package ru.tp_project.androidreader.model.repos

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.BookDb
import ru.tp_project.androidreader.model.data_models.Book
import javax.inject.Singleton

@Singleton
class BooksRepository {

    fun getBooks(context: Context, onResult: (isSuccess: Boolean, books: List<Book>) -> Unit) {
        GlobalScope.launch {
            var books = withContext(Dispatchers.Default) {
                BookDb.getInstance(context).booksDao().getAll()
            }
            onResult(true, books)
        }
    }

    fun loadBook(context: Context, book: Book, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                BookDb.getInstance(context).booksDao().addBook(book)
            }
            onResult(true)
        }
    }

    fun deleteBook(context: Context, book: Book, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                BookDb.getInstance(context).booksDao().deleteBook(book)
            }
            onResult(true)
        }
    }

    fun updateBook(context: Context, book: Book, onResult: (isSuccess: Boolean) -> Unit) {
        Log.d("updating", ""+book.pages +" "+ book.currPage)
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                BookDb.getInstance(context).booksDao().updateBook(book)
            }
            onResult(true)
        }
    }
}