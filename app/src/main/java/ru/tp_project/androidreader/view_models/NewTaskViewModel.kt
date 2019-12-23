package ru.tp_project.androidreader.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.repos.TasksRepository
import ru.tp_project.androidreader.model.repos.UserRepository


class NewTaskViewModel(val context: Context) : BaseViewModel() {
    private val selectedBooks = MutableLiveData<ArrayList<Book>>()

    init {
        selectedBooks.value = arrayListOf(
            Book(1, "BookName", "???", "Name Secname",
                "12.12.2012","kotlin", "no","100Kb", "fb2",
                0.5f, "what", "no path", 1,0),
            Book(2, "BookName", "???", "Name Secname",
                "12.12.2012","kotlin", "no","100Kb", "fb2",
                0.5f, "what", "no path", 1,0),
            Book(3, "BookName", "???", "Name Secname",
                "12.12.2012","kotlin", "no","100Kb", "fb2",
                0.5f, "what", "no path", 1,0)
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
        val userId = UserRepository().getCurrentUserID(context)
        TasksRepository.getInstance()
            .createTask(context, userId, taskName, selectedBooks.value!!.toList())
    }

    fun removeBook(book: Book) {
        selectedBooks.value!!.remove(book)
        selectedBooks.value = selectedBooks.value

        if (selectedBooks.value!!.size == 0) {
            empty.value = true
        }
    }
}