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
class BooksRepository {

    fun getBooks(context: Context, onResult: (isSuccess: Boolean, books: List<Book>) -> Unit) {
        GlobalScope.launch {
            val books = withContext(Dispatchers.Default) {
                AppDb.getInstance(context).booksDao().getAll()
            }
            onResult(true, books)
        }
    }

    fun loadBook(context: Context, book: Book, onResult: (id: Long, isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            val id = withContext(Dispatchers.Default) {
                AppDb.getInstance(context).booksDao().addBook(book)
            }
            onResult(id, true)
        }
    }

    fun deleteBook(context: Context, bookID: Int, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                AppDb.getInstance(context).booksDao().deleteBook(bookID)
            }
            onResult(true)
        }
    }
}