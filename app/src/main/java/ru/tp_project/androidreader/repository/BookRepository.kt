package ru.tp_project.androidreader.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.data.database.BookDao
import ru.tp_project.androidreader.data.database.BookDb
import ru.tp_project.androidreader.data.firebase.Webservice
import ru.tp_project.androidreader.models.Book
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository {
    private var bookID = "1"

    fun getBook(context: Context, onResult: (isSuccess: Boolean, book: Book?) -> Unit) {
        GlobalScope.launch {
            var book = withContext(Dispatchers.Default) {
                BookDb.getInstance(context).bookDao().load(bookID)
            }
            onResult(true, book)
        }
    }
}