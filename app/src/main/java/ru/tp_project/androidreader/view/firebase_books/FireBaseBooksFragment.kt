package ru.tp_project.androidreader.view.firebase_books

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_book_shelve.*
import kotlinx.android.synthetic.main.fragment_fire_base_books.*
import kotlinx.android.synthetic.main.view_fire_base_books_list_book.*
import ru.tp_project.androidreader.ReaderApp
import ru.tp_project.androidreader.databinding.FragmentFireBaseBooksBinding

class FireBaseBooksFragment : Fragment() {
    private lateinit var databinding: FragmentFireBaseBooksBinding
    private lateinit var adapter: FireBaseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databinding = FragmentFireBaseBooksBinding.inflate(inflater, container, false).apply {
            firebaseViewModel = ViewModelProviders.of(this@FireBaseBooksFragment).get(FireBaseViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return databinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databinding.firebaseViewModel?.fetchBooksList(ReaderApp.getInstance())
        setupAdapter()
        setupObservers()
    }

    private fun setupObservers() {
        databinding.firebaseViewModel?.booksListLive?.observe(viewLifecycleOwner, Observer {
            adapter.updateBooksList(it)
        })

        databinding.firebaseViewModel?.changed?.observe(viewLifecycleOwner, Observer {
            adapter.updateBooksList(databinding.firebaseViewModel?.booksListLive?.value!!.toList())
        })

    }

    private fun setupAdapter() {
        val viewModel = databinding.firebaseViewModel
        if (viewModel != null) {
            adapter = FireBaseListAdapter(databinding.firebaseViewModel!!,
            { bookLink, callback:() -> Unit ->
                onDeleteBook(bookLink, callback)
            }, {bookName, bookLink, callback:() -> Unit ->
                onDownloadBook(bookName, bookLink, callback)
            })

            val layoutManager = LinearLayoutManager(this.context)
            firebaseBooksListRv.layoutManager = layoutManager
            firebaseBooksListRv.addItemDecoration(DividerItemDecoration(this.context, layoutManager.orientation))
            firebaseBooksListRv.adapter = adapter
        }
    }

    private fun onDeleteBook(bookLink: String, callback: () -> Unit) {
        Log.println(Log.INFO, "kek", "delete")
        databinding.firebaseViewModel?.deleteBook(ReaderApp.getInstance(), bookLink) {
            callback()
        }
    }

    private fun onDownloadBook(bookName: String, bookLink: String, callback: () -> Unit) {
        Log.println(Log.INFO, "kek", "download")
        databinding.firebaseViewModel?.downloadBook(ReaderApp.getInstance(), bookName, bookLink) {
            callback()
        }
    }
}
