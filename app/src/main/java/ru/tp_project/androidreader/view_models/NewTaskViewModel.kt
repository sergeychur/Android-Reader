package ru.tp_project.androidreader.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.repos.BooksRepository
import ru.tp_project.androidreader.model.repos.TasksRepository
import ru.tp_project.androidreader.model.repos.UserRepository
import ru.tp_project.androidreader.view.task_books_choise_list.BooksChoiceViewHolder


class NewTaskViewModel : BaseViewModel() {
    private val selectedBooks = MutableLiveData<ArrayList<Book>>()
    val booksFromShelf = MutableLiveData<List<BooksChoiceViewHolder.SelectableItem>>()

    init {
        empty.value = true
        booksFromShelf.value = emptyList()
    }

    fun getSelectedBooks(): LiveData<ArrayList<Book>> {
        return selectedBooks
    }

    fun validateTask(taskName: String): Boolean {
        return taskName.isNotEmpty() && selectedBooks.value!!.size > 0
    }

    fun addTask(taskName: String, context: Context) {
        val userId = UserRepository().getCurrentUserID(context)
        TasksRepository.getInstance()
            .createTask(context, userId, taskName, selectedBooks.value!!.toList())
    }

    fun getBooks(context: Context) {
        BooksRepository().getBooks(context) { isSuccess, shelfBooks ->
            if (isSuccess) {
                val list: MutableList<BooksChoiceViewHolder.SelectableItem> =
                    emptyList<BooksChoiceViewHolder.SelectableItem>().toMutableList()
                for (book in shelfBooks) {
                    if (selectedBooks.value != null && selectedBooks.value?.contains(book)!!) {
                        list.add(BooksChoiceViewHolder.SelectableItem(book, true))
                    } else {
                        list.add(BooksChoiceViewHolder.SelectableItem(book, false))
                    }
                }
                booksFromShelf.postValue(list)
                empty.postValue(shelfBooks.isEmpty())
            } else {
                empty.postValue(true)
            }
        }
    }

    fun setBooks(books: List<Book>) {
        empty.postValue(books.isEmpty())
        selectedBooks.value = books.toMutableList() as ArrayList<Book>?
    }

    fun removeBook(book: Book) {
        selectedBooks.value!!.remove(book)
        selectedBooks.value = selectedBooks.value

        if (selectedBooks.value!!.size == 0) {
            empty.value = true
        }
    }
}