package ru.tp_project.androidreader.view.firebase_books

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.data_models.FireBaseBook
import ru.tp_project.androidreader.model.data_models.Pages
import ru.tp_project.androidreader.model.repos.BookRepository
import ru.tp_project.androidreader.model.repos.BooksRepository
import ru.tp_project.androidreader.model.repos.PagesRepository
import java.io.File


class FireBaseViewModel : BaseViewModel() {
    private var bookRepository = BooksRepository()
    private var pagesRep = PagesRepository()
    val booksListLive = MutableLiveData<MutableList<FireBaseBook>>()
    val changed = MutableLiveData<Boolean>()

    var data = MutableLiveData<List<Book>>()
    var pages = MutableLiveData<List<Pages>>()

    fun fetchBooksList(context: Context) {
        dataLoading.postValue(true)
        bookRepository.getFireBaseBooksList { isSuccess, books ->
            dataLoading.postValue(false)
            if (isSuccess) {
                booksListLive.postValue(books!!.toMutableList())
                empty.postValue(false)
            } else {
                empty.postValue(true)
            }
        }
    }
    fun deleteBook(context: Context, bookLink: String, successCallback: ()-> Unit,
                   failCallback: () -> Unit) {
        bookRepository.deleteFireBaseBook(bookLink, context) { isSuccess ->
            if (isSuccess) {
                booksListLive.value?.removeAll { book -> book.link == bookLink }
                successCallback()
                val changedVal = changed.value ?: false
                changed.postValue(!changedVal)
            } else {
                failCallback()
            }
        }
    }

    fun downloadBook(context: Context, bookName: String, bookLink: String, successCallback: ()-> Unit,
                     failCallback: ()-> Unit) {
        val dirname = context.filesDir!!.absolutePath
        val file = File(dirname, bookName)
        // create a new file
        val isNewFileCreated :Boolean = file.createNewFile()

        if(isNewFileCreated){
            Log.println(Log.INFO, "INFO", "$bookName is created successfully.")
        } else{
            Log.println(Log.ERROR, "ERROR", "$bookName already exists.")
        }
        bookRepository.getFireBaseBook(bookLink, file) { isSuccess ->
            if (isSuccess) {
                Log.println(Log.INFO, "kek", "success")
                successCallback()
            } else {
                Log.println(Log.ERROR, "kek", "fail")
                failCallback()
            }
        }
    }

    fun load(context: Context, book: Book, pages: Pages, action : (id: Long) -> Unit) {
        startMultiple(2)
        bookRepository.loadBook(context, book) { id, isSuccess ->
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
}