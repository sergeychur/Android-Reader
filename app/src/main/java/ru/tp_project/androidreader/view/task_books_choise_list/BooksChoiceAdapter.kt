package ru.tp_project.androidreader.view.task_books_choise_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.databinding.TaskBooksSelectionListItemBinding
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.view.task_books_choise_list.BooksChoiceViewHolder.SelectableItem


class BooksChoiceAdapter :
    RecyclerView.Adapter<BooksChoiceViewHolder>(), BooksChoiceViewHolder.OnItemSelectedListener {
    private var booksList: ArrayList<SelectableItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksChoiceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = TaskBooksSelectionListItemBinding.inflate(inflater, parent, false)
        return BooksChoiceViewHolder(dataBinding)
    }

    override fun getItemCount() = booksList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: BooksChoiceViewHolder, position: Int) {
        holder.bind(booksList[position], this)
        holder.itemView.setOnClickListener {
            holder.setChecked(!holder.data!!.isSelected)
        }
    }

    fun updateBooksList(booksList: List<Book>) {
        this.booksList.clear()
        for (book in booksList) {
            this.booksList.add(SelectableItem(book, false))
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

    override fun onItemSelected(item: SelectableItem?) {

    }
}