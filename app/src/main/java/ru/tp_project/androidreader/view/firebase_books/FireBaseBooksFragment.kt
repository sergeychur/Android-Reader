package ru.tp_project.androidreader.view.firebase_books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_fire_base_books.*
import ru.tp_project.androidreader.ReaderApp
import ru.tp_project.androidreader.databinding.FragmentFireBaseBooksBinding

class FireBaseBooksFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentFireBaseBooksBinding
    private lateinit var adapter: FireBaseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentFireBaseBooksBinding.inflate(inflater, container, false).apply {
            firebaseViewModel = ViewModelProviders.of(this@FireBaseBooksFragment).get(FireBaseViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.firebaseViewModel?.fetchBooksList(ReaderApp.getInstance())
        setupAdapter()
        setupObservers()
    }

    private fun setupObservers() {
        viewDataBinding.firebaseViewModel?.booksListLive?.observe(viewLifecycleOwner, Observer {
            adapter.updateBooksList(it)
        })

        viewDataBinding.firebaseViewModel?.changed?.observe(viewLifecycleOwner, Observer {
            adapter.updateBooksList(viewDataBinding.firebaseViewModel?.booksListLive?.value!!.toList())
        })

    }

    private fun setupAdapter() {
        val viewModel = viewDataBinding.firebaseViewModel
        if (viewModel != null) {
            adapter = FireBaseListAdapter(viewDataBinding.firebaseViewModel!!
            ) { bookLink, callback:() -> Unit ->
                onDeleteBook(bookLink, callback)
            }
            val layoutManager = LinearLayoutManager(activity)
            fire_base_books_list_rv.layoutManager = layoutManager
            fire_base_books_list_rv.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))
            fire_base_books_list_rv.adapter = adapter
        }
    }

    private fun onDeleteBook(bookLink: String, callback: () -> Unit) {
        viewDataBinding.firebaseViewModel?.deleteBook(ReaderApp.getInstance(), bookLink) {
            callback()
        }
    }

    private fun onDownloadBook(bookLink: String, callback: () -> Unit) {
        viewDataBinding.firebaseViewModel?.downloadBook(ReaderApp.getInstance(), bookLink) {
            callback()
        }
    }
}
