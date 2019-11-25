package ru.tp_project.androidreader.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_new_task.*
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.databinding.FragmentNewTaskBinding
import ru.tp_project.androidreader.view.books_choise_list.BooksChoiceListAdapter
import ru.tp_project.androidreader.view_models.BaseViewModelFactory
import ru.tp_project.androidreader.view_models.NewTaskViewModel

class NewTaskFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentNewTaskBinding
    private lateinit var adapter: BooksChoiceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentNewTaskBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProviders.of(
                this@NewTaskFragment,
                BaseViewModelFactory { NewTaskViewModel(context!!.applicationContext) })
                .get(NewTaskViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_book_to_tusk_btn.setOnClickListener { v: View ->
            run {
                v.visibility = View.INVISIBLE
                hided_btns.visibility = View.VISIBLE
            }
        }
        hide_btn.setOnClickListener {
            run {
                hided_btns.visibility = View.INVISIBLE
                add_book_to_tusk_btn.show()
            }
        }
        setupAdapter()
        setupObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_accept).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_accept) {
            val taskName = task_name_input.text.toString()
            if (viewDataBinding.viewmodel!!.validateTask(taskName)) {
                viewDataBinding.viewmodel!!.addTask(taskName)
                findNavController().navigateUp()
            } else {
                showInvalidTaskAlert()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showInvalidTaskAlert() {
        val alertDialog = AlertDialog.Builder(activity)
            .setMessage(getString(R.string.task_add_alert_text))
            .setTitle(getString(R.string.task_add_alert_header))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.cancel() }.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(
                ContextCompat.getColor(
                    context!!.applicationContext,
                    R.color.colorPrimaryBlue
                )
            )
    }

    private fun setupObservers() {
        viewDataBinding.viewmodel?.getSelectedBooks()?.observe(viewLifecycleOwner, Observer {
            adapter.updateBooksList(it)
        })
    }

    private fun setupAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            adapter = BooksChoiceListAdapter(viewDataBinding.viewmodel!!)
            val layoutManager = LinearLayoutManager(activity)
            selected_books_rv.layoutManager = layoutManager
            selected_books_rv.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    layoutManager.orientation
                )
            )
            selected_books_rv.adapter = adapter
        }
    }

}
