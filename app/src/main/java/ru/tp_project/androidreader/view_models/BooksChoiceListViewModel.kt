package ru.tp_project.androidreader.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.repos.BooksRepository


class BooksChoiceListViewModel : BaseViewModel() {
    private val selectedBooks = MutableLiveData<ArrayList<Book>>()
    private val booksListLive = MutableLiveData<List<Book>>()

    init {
        selectedBooks.value = ArrayList()//arrayListOf(Book("1"), Book("2"))
        empty.value = true // TODO (Kotyrich) temprorary
    }

    fun getBooksList() {
        dataLoading.value = true
        BooksRepository.getInstance().getBooksList { isSuccess, books ->
            dataLoading.value = false
            if (isSuccess) {
                booksListLive.value = books
                empty.value = false
            } else {
                empty.value = true
            }
        }
    }

    fun getSelectedBooks(): LiveData<ArrayList<Book>> {
        return selectedBooks
    }

    fun addSelectedBook(book: Book) {
        selectedBooks.value!!.add(book)
        empty.value = false
    }
}

data class Book(val id: String)