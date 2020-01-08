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
    var pages = MutableLiveData<List<Pages>>()
    private var booksRep = BooksRepository()
    private var pagesRep = PagesRepository()

    // get list of books
    fun getAll(context: Context) {
        startMultiple(2)
        booksRep.getBooks(context) { isSuccess, books ->
            if (isSuccess) {
                data.postValue(books)
            }
            finishMultiple(isSuccess)
        }

        pagesRep.getAll(context) { isSuccess, getPages ->
            if (isSuccess) {
                pages.postValue(getPages)
            }
            finishMultiple(isSuccess)
        }
    }

    // add new book and it's pages
    fun load(context: Context, book: Book, pages: Pages, action : (id: Long) -> Unit) {
        startMultiple(2)
        booksRep.loadBook(context, book) { id, isSuccess ->
            if (isSuccess) {
                val list = data.value
                list?.let {
                    val arr = list.toMutableList()
                    book.id=id.toInt()
                    arr.add(book)
                    data.postValue(arr)
                }
            }
            finishMultiple(isSuccess)
            pages.bookID=id.toInt()
            pagesRep.load(context, pages) {isSuccess2 ->
                val list = this.pages.value
                list?.let {
                    val arr = list.toMutableList()
                    book.id=id.toInt()
                    arr.add(pages)
                    this.pages.postValue(arr)
                }

                finishMultiple(isSuccess2)
                action(id)

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
            val list = this.pages.value
            list?.let {
                val arr = list.toMutableList()
                val pages = arr.find { pages -> pages.bookID==bookID }
                arr.remove(pages)
                this.pages.postValue(arr)
            }
            finishMultiple(isSuccess)
        }
    }
}