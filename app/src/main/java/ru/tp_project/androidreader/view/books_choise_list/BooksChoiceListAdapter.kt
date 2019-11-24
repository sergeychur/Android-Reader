package ru.tp_project.androidreader.view.books_choise_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.databinding.ViewBooksChoiceListBookBinding
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.view_models.NewTaskViewModel

class BooksChoiceListAdapter(private val newTaskViewModel: NewTaskViewModel) :
    RecyclerView.Adapter<BooksChoiceListViewHolder>() {
    private var booksList: List<Book> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksChoiceListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ViewBooksChoiceListBookBinding.inflate(inflater, parent, false)
        return BooksChoiceListViewHolder(dataBinding, newTaskViewModel)
    }

    override fun getItemCount() = booksList.size

    override fun onBindViewHolder(holder: BooksChoiceListViewHolder, position: Int) {
        holder.setup(booksList[position])
        holder.itemView.findViewById<Button>(R.id.delete_btn).setOnClickListener { v: View ->
            run {
                newTaskViewModel.removeBook(booksList[position])
            }
        }
    }

    fun updateBooksList(booksList: List<Book>) {
        this.booksList = booksList
        notifyDataSetChanged()
    }
}