package ru.tp_project.androidreader.view.task_books_choise_list


import android.content.ClipData.Item
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.BR
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.Book


class BooksChoiceViewHolder(private val dataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(dataBinding.root) {

    fun bind(itemData: Book, isActivated: Boolean = false) {
        dataBinding.setVariable(BR.data, itemData.name)
        dataBinding.executePendingBindings()
        itemView.findViewById<TextView>(R.id.book_choice_name).text = itemData.name
        itemView.isActivated = isActivated
    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
        object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = adapterPosition
            override fun getSelectionKey(): Long? = itemId
        }

    class SelectableItem(val book: Book, isSelected: Boolean) {
        var isSelected = false

        init {
            this.isSelected = isSelected
        }
    }
}