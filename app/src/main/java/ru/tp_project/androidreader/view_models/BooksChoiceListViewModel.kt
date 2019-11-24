package ru.tp_project.androidreader.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.repos.BooksRepository


class BooksChoiceListViewModel : BaseViewModel() {
    private val selectedBooks = MutableLiveData<ArrayList<Book>>()
    private val booksListLive = MutableLiveData<List<Book>>()

    init {
        selectedBooks.value = arrayListOf(
            Book("1", "BookName", "???", "Name Secname", 100.13f, "fb2", 0.5f, "what"),
            Book("1", "BookName", "???", "Name Secname", 100.13f, "fb2", 0.5f, "what"),
            Book("1", "BookName", "???", "Name Secname", 100.13f, "fb2", 0.5f, "what")
        )
        empty.value = false // TODO(Kotyrich) temporary
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

    fun removeBook(book: Book) {
        selectedBooks.value!!.remove(book)
        selectedBooks.value = selectedBooks.value

        if (selectedBooks.value!!.size == 0) {
            empty.value = true
        }
    }
}