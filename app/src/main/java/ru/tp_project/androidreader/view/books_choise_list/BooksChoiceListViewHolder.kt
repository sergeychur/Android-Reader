package ru.tp_project.androidreader.view.books_choise_list

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


import ru.tp_project.androidreader.BR
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.Book

class BooksChoiceListViewHolder(
    private val dataBinding: ViewDataBinding
) : RecyclerView.ViewHolder(dataBinding.root) {

    fun setup(itemData: Book) {
        dataBinding.setVariable(BR.data, itemData)
        dataBinding.executePendingBindings()
        itemData.setImageToImageView(itemView.findViewById(R.id.bookPreview))
    }
}