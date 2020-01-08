package ru.tp_project.androidreader.view_models

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.data_models.Pages
import ru.tp_project.androidreader.model.repos.BooksRepository
import ru.tp_project.androidreader.model.repos.PagesRepository

class BooksShelveViewModel : BaseViewModel() {
    var data = MutableLiveData<List<Book>>()
    private var booksRep = BooksRepository()
    private var pagesRep = PagesRepository()

    // get list of books
    fun getAll(context: Context) {
        start()
        booksRep.getBooks(context) { isSuccess, books ->
            if (isSuccess) {
                Log.d("we get book", "book:$books")
                data.postValue(books)
            }
            finish(isSuccess)
        }
    }

    // add new book and it's pages
    fun load(context: Context, book: Book, pages: Pages) {
        startMultiple(2)
        booksRep.loadBook(context, book) { isSuccess ->
            if (isSuccess) {
                val list = data.value
                list?.let {
                    val arr = list.toMutableList()
                    arr.add(book)
                    data.postValue(arr)
                }
            }
            finishMultiple(isSuccess)
            pages.bookID=book.id
            pagesRep.load(context, pages) {isSuccess2 ->
                finishMultiple(isSuccess2)
            }
        }
    }

    // delete book and it's pages
    fun delete(context: Context, bookID: Int) {
        startMultiple(2)
        booksRep.deleteBook(context, bookID) { isSuccess ->
            if (isSuccess) {
                val list = data.value
                list?.let {
                    val arr = list.toMutableList()
                    val book = arr.find { book -> book.id==bookID }
                    arr.remove(book)
                    data.postValue(arr)
                }
            }
            finishMultiple(isSuccess)
        }
        pagesRep.delete(context, bookID) {isSuccess ->
            finishMultiple(isSuccess)
        }
    }
}