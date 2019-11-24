package ru.tp_project.androidreader.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_new_task.*

import ru.tp_project.androidreader.databinding.FragmentNewTaskBinding
import ru.tp_project.androidreader.view.books_choise_list.BooksChoiceListAdapter
import ru.tp_project.androidreader.view_models.BooksChoiceListViewModel

class NewTaskFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentNewTaskBinding
    private lateinit var adapter: BooksChoiceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentNewTaskBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProviders.of(this@NewTaskFragment).get(BooksChoiceListViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupObservers()
    }

    private fun setupObservers() {
        viewDataBinding.viewmodel?.getSelectedBooks()?.observe(viewLifecycleOwner, Observer {
            Log.d("Mytag", "update adapter")
            adapter.updateBooksList(it)
        })
    }

    private fun setupAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            adapter = BooksChoiceListAdapter(viewDataBinding.viewmodel!!)
            val layoutManager = LinearLayoutManager(activity)
            selected_books_rv.layoutManager = layoutManager
            selected_books_rv.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))
            selected_books_rv.adapter = adapter
        }
    }

}
