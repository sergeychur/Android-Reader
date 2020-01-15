package ru.tp_project.androidreader.model.repos

import android.content.Context
import android.os.Environment
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.data_models.FireBaseBook
import ru.tp_project.androidreader.model.firebase.FileStorage
import java.io.File
import javax.inject.Singleton

@Singleton
class BooksRepository {

    fun getBooks(context: Context, onResult: (isSuccess: Boolean, books: List<Book>) -> Unit) {
        GlobalScope.launch {
            val books = withContext(Dispatchers.Default) {
                AppDb.getInstance(context).booksDao().getAll().reversed()
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

    fun getFireBaseBooksList(onResult: (isSuccess: Boolean, books: List<FireBaseBook>?) -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val isPublic = userId == null
                FileStorage.getInstance().listFiles(isPublic = isPublic, userId = userId,
                    successCallback = {names, pathes ->
                        val result = MutableList(0) { FireBaseBook("", "") }
                        for (x in 0 until names.size) {
                            result.add(FireBaseBook(names[x], pathes[x]))
                        }
                        onResult(result.isNotEmpty(), result)
                    },
                    failCallback = {onResult(false, null)},
                    pageToken = null)
            }
        }
    }

    fun deleteFireBaseBook(bookLink: String, context: Context, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            FileStorage.getInstance().deleteFile(bookLink,
                successCallback = {
                    Log.println(Log.INFO, "success", "YOOHOO")
                    onResult(true)
                },
                failCallback = {
                    Log.println(Log.ERROR, "exception", it.message!!)
                    onResult(false)
                })
        }
    }

    fun getFireBaseBook(bookLink: String, destination: File, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            FileStorage.getInstance().downloadFile(bookLink,
                successCallback = {
                    Log.d("dad", "download success")
                    onResult(true)
                },
                failCallback = {
                    Log.println(Log.ERROR, "exception", it.message!!)
                    Log.d("dad", "download fail")
                    onResult(false)
                },
                destination = destination)

        }
    }

    fun uploadOnFireBase(book: Book, onResult: (isSuccess: Boolean) -> Unit) {
        val bookFile = File(book.path)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            onResult(false)
            return
        }
        GlobalScope.launch {
            FileStorage.getInstance().uploadFile(book = bookFile,
                userId = userId,
                successCallback = {
                    Log.d("dad", "upload success")
                    onResult(true)
                },
                failCallback = {
                    Log.println(Log.ERROR, "exception", it.message!!)
                    Log.d("dad", "upload fail")
//                    onResult(false)
                })

        }
    }
}