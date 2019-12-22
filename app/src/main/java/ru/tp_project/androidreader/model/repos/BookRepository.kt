package ru.tp_project.androidreader.model.repos

import android.content.Context
import android.util.Log
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
import java.io.File
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
            /*withContext(Dispatchers.Default) {
                FileStorage.getInstance().listFiles(isPublic = true, userId = FirebaseAuth.getInstance().currentUser!!.uid,
                    successCallback = {names, pathes ->
                        val result = MutableList(0) { FireBaseBook("", "") }
                        for (x in 0..names.size) {
                            result.add(FireBaseBook(names[x], pathes[x]))
                        }
                        onResult(result.isNotEmpty(), result)
                    },
                    failCallback = {onResult(false, null)},
                    pageToken = null)
            }*/
            onResult(true, listOf(FireBaseBook("w", "w"), FireBaseBook("w", "w")))

        }
    }

    fun deleteFireBaseBook(bookLink: String, context: Context, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            FileStorage.getInstance().deleteFile(bookLink,
                successCallback = {
                    Log.println(Log.INFO, "success", "YOOHOO")
                },
                failCallback = {
                    Log.println(Log.ERROR, "exception", it.message!!)
                })
            onResult(true)
        }
    }

    fun getFireBaseBook(bookLink: String, destination: File, context: Context, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            FileStorage.getInstance().downloadFile(bookLink,
                successCallback = {

                },
                failCallback = {
                    Log.println(Log.ERROR, "exception", it.message!!)
                },
                destination = destination)
            onResult(true)
        }
    }
}