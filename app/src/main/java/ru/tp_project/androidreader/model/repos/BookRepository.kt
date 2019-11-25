package ru.tp_project.androidreader.model.repos

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.data_models.Book
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
}