package ru.tp_project.androidreader.view.books_choise_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.databinding.ViewBooksChoiceListBookBinding
import ru.tp_project.androidreader.view_models.Book
import ru.tp_project.androidreader.view_models.BooksChoiceListViewModel

class BooksChoiceListAdapter(private val booksChoiceListViewModel: BooksChoiceListViewModel) :
    RecyclerView.Adapter<BooksChoiceListViewHolder>() {
    var booksList: List<Book> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksChoiceListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ViewBooksChoiceListBookBinding.inflate(inflater, parent, false)
        return BooksChoiceListViewHolder(dataBinding, booksChoiceListViewModel)
    }

    override fun getItemCount() = booksList.size

    override fun onBindViewHolder(holder: BooksChoiceListViewHolder, position: Int) {
        holder.setup(booksList[position])
    }

    fun updateBooksList(booksList: List<Book>) {
        this.booksList = booksList
        notifyDataSetChanged()
    }
}