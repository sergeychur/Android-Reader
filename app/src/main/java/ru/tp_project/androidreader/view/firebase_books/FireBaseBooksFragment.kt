package ru.tp_project.androidreader.view.firebase_books

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_fire_base_books.*
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.ReaderApp
import ru.tp_project.androidreader.databinding.FragmentFireBaseBooksBinding
import ru.tp_project.androidreader.utils.*
import java.io.File

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
            { bookLink ->
                onDeleteBook(bookLink)
            },
            {bookName, bookLink ->
                onDownloadBook(bookName, bookLink)
            })

            val layoutManager = LinearLayoutManager(this.context)
            firebaseBooksListRv.layoutManager = layoutManager
            firebaseBooksListRv.addItemDecoration(DividerItemDecoration(this.context, layoutManager.orientation))
            firebaseBooksListRv.adapter = adapter
        }
    }

    private fun onDeleteBook(bookLink: String) {
        Log.println(Log.INFO, "kek", "delete")
        val successCallback = {
            Toast.makeText(
                activity,
                getText(R.string.delete_success),
                Toast.LENGTH_LONG
            ).show()
        }
        val failCallback = {
            Toast.makeText(
                activity,
                getText(R.string.delete_fail),
                Toast.LENGTH_LONG
            ).show()
        }
        databinding.firebaseViewModel?.deleteBook(ReaderApp.getInstance(), bookLink,
            successCallback, failCallback)
    }

    @SuppressLint("SdCardPath")
    private fun onDownloadBook(bookName: String, bookLink: String) {
        Log.println(Log.INFO, "kek", "download")
        val dirname = ReaderApp.getInstance().DOWNLOAD_PATH
        Toast.makeText(
            activity,
            getText(R.string.download_started),
            Toast.LENGTH_LONG
        ).show()
        val successCallback = {
            val viewModel = checkNotNull(databinding.firebaseViewModel)
            launchBook(this, Uri.fromFile(File(dirname, bookName)), {}, viewModel::load)
        }

        val failCallback = {
            Toast.makeText(
                activity,
                getText(R.string.failed_to_download),
                Toast.LENGTH_LONG
            ).show()
        }

        databinding.firebaseViewModel?.downloadBook(
            bookName,
            bookLink,
            successCallback,
            failCallback
        )
    }
}
