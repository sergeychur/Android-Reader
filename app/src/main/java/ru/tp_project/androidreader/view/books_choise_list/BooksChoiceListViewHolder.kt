package ru.tp_project.androidreader.view.books_choise_list

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


import ru.tp_project.androidreader.BR
import ru.tp_project.androidreader.view_models.Book
import ru.tp_project.androidreader.view_models.BooksChoiceListViewModel

class BooksChoiceListViewHolder(
    private val dataBinding: ViewDataBinding,
    private val booksListViewModel: BooksChoiceListViewModel
) : RecyclerView.ViewHolder(dataBinding.root) {

    fun setup(itemData: Book) {
        dataBinding.setVariable(BR.data, itemData)
        dataBinding.executePendingBindings()
    }
}