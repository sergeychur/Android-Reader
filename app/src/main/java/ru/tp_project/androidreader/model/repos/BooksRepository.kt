package ru.tp_project.androidreader.model.repos

import ru.tp_project.androidreader.view_models.Book

class BooksRepository {

    fun getBooksList(onResult: (isSuccess: Boolean, tasks: List<Book>?) -> Unit) {
        // TODO(me): change implementation
        val books: List<Book> = listOf(Book("1"), Book("2"), Book("3"))
        onResult(true, books)
    }

    companion object {
        private var INSTANCE: BooksRepository? = null
        fun getInstance() = INSTANCE
            ?: BooksRepository().also {
                INSTANCE = it
            }
    }
}