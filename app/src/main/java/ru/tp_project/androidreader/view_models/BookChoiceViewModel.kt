package ru.tp_project.androidreader.view_models

import android.content.Context
import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.repos.BooksRepository


class BookChoiceViewModel() : BaseViewModel() {
    val books = MutableLiveData<List<Book>>()

    init {
        empty.value = true
    }

    fun getBooks(context: Context) {
        BooksRepository().getBooks(context) {isSuccess, shelf_books ->
            if (isSuccess) {
                books.postValue(shelf_books)
                empty.postValue(shelf_books.isNotEmpty())
            } else {
                empty.postValue(true)
            }
        }
    }
}