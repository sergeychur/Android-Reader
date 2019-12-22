package ru.tp_project.androidreader.view.firebase_books

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.FireBaseBook
import ru.tp_project.androidreader.model.repos.BookRepository

class FireBaseViewModel : BaseViewModel() {
    private var repository = BookRepository()
    val booksListLive = MutableLiveData<MutableList<FireBaseBook>>()
    val changed = MutableLiveData<Boolean>()

    fun clearBooksList() {
        booksListLive.value?.clear()
    }

    fun fetchBooksList(context: Context) {
        dataLoading.postValue(true)
        repository.getFireBaseBooksList(context) { isSuccess, books ->
            dataLoading.postValue(false)
            if (isSuccess) {
                booksListLive.postValue(books?.toMutableList())
                empty.postValue(false)
            } else {
                empty.postValue(true)
            }
        }
    }
    fun deleteBook(context: Context, bookLink: String, successCallback: ()-> Unit) {
        Log.d("kek", "deleted")
        repository.deleteFireBaseBook(bookLink, context) {isSuccess ->
            if (isSuccess) {
                booksListLive.value?.removeAll { book -> book.link == bookLink }
                successCallback()
                val changedVal = changed.value ?: false
                changed.postValue(!changedVal)
            }
        }
    }

    fun downloadBook(context: Context, bookLink: String, successCallback: ()-> Unit) {
        repository.getFireBaseBook(bookLink, context) { isSuccess ->
            if (isSuccess) {
                Log.println(Log.INFO, "kek", "success")
            } else {
                Log.println(Log.ERROR, "kek", "fail")
            }
        }
    }
}