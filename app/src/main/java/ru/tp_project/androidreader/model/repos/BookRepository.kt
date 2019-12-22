package ru.tp_project.androidreader.model.repos

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.data_models.FireBaseBook
import ru.tp_project.androidreader.model.firebase.FileStorage
import javax.inject.Singleton

@Singleton
class BookRepository {
    private var bookID = "1"

    fun getBook(context: Context, onResult: (isSuccess: Boolean, book: Book?) -> Unit) {
        GlobalScope.launch {
            val book = withContext(Dispatchers.Default) {
                AppDb.getInstance(context).bookDao().load(bookID)
            }
            onResult(true, book)
        }
    }

    fun getFireBaseBooksList(context: Context, onResult: (isSuccess: Boolean, books: List<FireBaseBook>?) -> Unit) {
        GlobalScope.launch {
            val books = withContext(Dispatchers.Default) {
                listOf(FireBaseBook("w", "w"), FireBaseBook("w", "w"))
            }
            onResult(books.isNotEmpty(), books)
        }
    }

    fun deleteFireBaseBook(bookLink: String, context: Context, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            onResult(true)
        }
    }

    fun getFireBaseBook(bookLink: String, context: Context, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            onResult(true)
        }
    }
}