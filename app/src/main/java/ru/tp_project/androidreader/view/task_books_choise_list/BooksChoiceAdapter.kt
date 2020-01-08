package ru.tp_project.androidreader.view.task_books_choise_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.databinding.TaskBooksSelectionListItemBinding
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.view.task_books_choise_list.BooksChoiceViewHolder.SelectableItem


class BooksChoiceAdapter : RecyclerView.Adapter<BooksChoiceViewHolder>() {
    private var booksList: ArrayList<SelectableItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksChoiceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = TaskBooksSelectionListItemBinding.inflate(inflater, parent, false)
        return BooksChoiceViewHolder(dataBinding)
    }

    override fun getItemCount() = booksList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: BooksChoiceViewHolder, position: Int) {
        holder.bind(booksList[position])
        holder.itemView.setOnClickListener {
            holder.setChecked(!holder.data!!.isSelected)
        }
    }

    fun updateBooksList(booksList: List<SelectableItem>) {
        if (booksList.isEmpty()) {
            this.booksList.clear()
        } else {
            this.booksList = booksList as ArrayList<SelectableItem>
        }
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<Book>? {
        val selectedItems: MutableList<Book> = ArrayList()
        for (item in booksList) {
            if (item.isSelected) {
                selectedItems.add(item.book)
            }
        }
        return selectedItems
    }
}