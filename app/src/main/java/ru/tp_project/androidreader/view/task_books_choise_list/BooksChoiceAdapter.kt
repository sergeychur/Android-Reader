package ru.tp_project.androidreader.view.task_books_choise_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.databinding.TaskBooksSelectionListItemBinding
import ru.tp_project.androidreader.model.data_models.Book

class BooksChoiceAdapter : RecyclerView.Adapter<BooksChoiceViewHolder>() {
    private var booksList: List<Book> = emptyList()
    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksChoiceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = TaskBooksSelectionListItemBinding.inflate(inflater, parent, false)
        return BooksChoiceViewHolder(dataBinding)
    }

    override fun getItemCount() = booksList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: BooksChoiceViewHolder, position: Int) {
        tracker?.let {
            holder.bind(booksList[position], it.isSelected(position.toLong()))
        }
    }

    fun updateBooksList(booksList: List<Book>) {
        this.booksList = booksList
        notifyDataSetChanged()
    }
}