package ru.tp_project.androidreader.model.repos

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.data_models.Pages
import javax.inject.Singleton

@Singleton
class PagesRepository {

    fun get(context: Context, id: Int, onResult: (isSuccess: Boolean, pages: Pages) -> Unit) {
        GlobalScope.launch {
            var pages = withContext(Dispatchers.Default) {
                AppDb.getInstance(context).pagesDao().get(id)
            }
            onResult(true, pages)
        }
    }

    fun load(context: Context, pages: Pages, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                AppDb.getInstance(context).pagesDao().add(pages)
            }
            onResult(true)
        }
    }

    fun delete(context: Context, bookID: Int, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                AppDb.getInstance(context).pagesDao().delete(bookID)
            }
            onResult(true)
        }
    }

    fun update(context: Context, pages: Pages, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                AppDb.getInstance(context).pagesDao().update(pages)
            }
            onResult(true)
        }
    }
}