package ru.tp_project.androidreader.view.task_books_choise_list


import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.BR
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.Book


class BooksChoiceViewHolder(private val dataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(dataBinding.root) {
    var data: SelectableItem? = null
    var itemSelectedListener: OnItemSelectedListener? = null

    fun bind(itemData: SelectableItem, listener: OnItemSelectedListener) {
        dataBinding.setVariable(BR.data, itemData.book.name)
        dataBinding.executePendingBindings()
        itemView.findViewById<TextView>(R.id.book_choice_name).text = itemData.book.name
        data = itemData
        itemSelectedListener = listener
    }

    fun setChecked(value: Boolean) {
        itemView.isActivated = value
        data?.isSelected = value
//        itemView.findViewById<TextView>(R.id.book_choice_name).setChecked(value)
    }

    class SelectableItem(val book: Book, isSelected: Boolean) {
        var isSelected = false

        init {
            this.isSelected = isSelected
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: SelectableItem?)
    }
}