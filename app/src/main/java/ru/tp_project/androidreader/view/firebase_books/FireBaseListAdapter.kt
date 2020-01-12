package ru.tp_project.androidreader.view.firebase_books

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.databinding.FragmentFireBaseBooksBinding
import ru.tp_project.androidreader.databinding.ViewFireBaseBooksListBookBinding
import ru.tp_project.androidreader.model.data_models.FireBaseBook

class FireBaseListAdapter(private val firebasebookViewModel: FireBaseViewModel,
                       private val deleteListener: (String) -> Unit,
                        private val downloadListener: (String, String) -> Unit)
    : RecyclerView.Adapter<FireBaseListViewHolder>() {
    var booksList: List<FireBaseBook> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FireBaseListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ViewFireBaseBooksListBookBinding.inflate(inflater, parent, false)
        return FireBaseListViewHolder(dataBinding)
    }

    override fun getItemCount() = booksList.size

    override fun onBindViewHolder(holder: FireBaseListViewHolder, position: Int) {
        holder.setup(booksList[position], deleteListener, downloadListener)
    }

    fun updateBooksList(booksList: List<FireBaseBook>) {
        this.booksList = booksList
        notifyDataSetChanged()
    }
}