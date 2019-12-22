package ru.tp_project.androidreader.view_models

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.repos.BookRepository
import ru.tp_project.androidreader.model.repos.BooksRepository
import javax.inject.Inject

// https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate
// https://www.fandroid.info/viewmodels-with-saved-state/
class BookShelveViewModel @Inject constructor(val context: Context): ViewModel() {
    var data = MutableLiveData<Book>()// LiveData<Book> = repository.getBook(userId!!)
    var repository = BookRepository()
    var all = BooksRepository()
    var fail = MutableLiveData<Boolean>().apply { value = false }

    fun refresh() {
        repository.getBook(context) { isSuccess, book ->
            if (isSuccess) {
                Log.d("we get book", "book:"+ book)
                data.postValue(book)
            } else {
                Log.d("we dont get book", "book:"+ book)
                fail.postValue(true)
            }
        }
    }
}

class BooksShelveViewModel : BaseViewModel() {
    var data = MutableLiveData<List<Book>>()
    var booksRep = BooksRepository()

    fun getAll(context: Context) {
        start()
        booksRep.getBooks(context) { isSuccess, books ->
            if (isSuccess) {
                Log.d("we get book", "book:"+ books)
                data.postValue(books)
            }
            finish(isSuccess)
        }
    }

    fun load(context: Context, book: Book) {
        start()
        booksRep.loadBook(context, book) { isSuccess ->
            if (isSuccess) {
                val list = data.value
                list?.let {
                    val arr = list.toMutableList()
                    arr.add(book)
                    data.postValue(arr)
                }
            }
            finish(isSuccess)
        }
    }
}