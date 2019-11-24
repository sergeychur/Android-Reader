package ru.tp_project.androidreader.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.repos.BooksRepository


class NewTaskViewModel : BaseViewModel() {
    private val selectedBooks = MutableLiveData<ArrayList<Book>>()

    init {
        selectedBooks.value = arrayListOf(
            Book("1", "BookName", "???", "Name Secname", 100.13f, "fb2", 0.5f, "what"),
            Book("1", "BookName", "???", "Name Secname", 100.13f, "fb2", 0.5f, "what"),
            Book("1", "BookName", "???", "Name Secname", 100.13f, "fb2", 0.5f, "what")
        )
        empty.value = false // TODO(Kotyrich) temporary
    }

    fun getSelectedBooks(): LiveData<ArrayList<Book>> {
        return selectedBooks
    }

    fun validateTask(taskName: String): Boolean {
        return taskName.isNotEmpty() && selectedBooks.value!!.size > 0
    }

    fun addTask(taskName: String) {

    }

    fun removeBook(book: Book) {
        selectedBooks.value!!.remove(book)
        selectedBooks.value = selectedBooks.value

        if (selectedBooks.value!!.size == 0) {
            empty.value = true
        }
    }
}