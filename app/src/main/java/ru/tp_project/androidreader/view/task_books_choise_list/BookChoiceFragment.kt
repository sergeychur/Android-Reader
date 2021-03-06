package ru.tp_project.androidreader.view.task_books_choise_list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_choice_book.*
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.databinding.FragmentChoiceBookBinding
import ru.tp_project.androidreader.view_models.NewTaskViewModel

class BookChoiceFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentChoiceBookBinding
    private lateinit var adapter: BooksChoiceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentChoiceBookBinding.inflate(inflater, container, false).apply {
            viewmodel = activity?.run {
                ViewModelProviders.of(this)[NewTaskViewModel::class.java]
            }
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.viewmodel!!.getBooks(context!!.applicationContext)

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
            adapter.getSelectedItems()?.let { viewDataBinding.viewmodel!!.setBooks(it) }
            findNavController().navigateUp()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupObservers() {
        viewDataBinding.viewmodel?.booksFromShelf?.observe(viewLifecycleOwner, Observer {
            adapter.updateBooksList(it)
        })
        viewDataBinding.viewmodel?.emptyToChoose?.observe(viewLifecycleOwner, Observer {
            books_to_choose_empty_text.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun setupAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            adapter = BooksChoiceAdapter()
            val layoutManager = LinearLayoutManager(activity)
            books_choice_rv.layoutManager = layoutManager
            books_choice_rv.addItemDecoration(
                DividerItemDecoration(activity, layoutManager.orientation)
            )
            books_choice_rv.adapter = adapter
        }
    }

}
