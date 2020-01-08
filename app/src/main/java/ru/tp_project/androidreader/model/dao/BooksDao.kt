package ru.tp_project.androidreader.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.tp_project.androidreader.model.data_models.Book

@Dao
interface BooksDao {
    @Query("SELECT * FROM book")
    suspend fun getAll(): List<Book>
    @Insert
    suspend fun addBook(book: Book) : Long

    @Query("DElETE FROM book WHERE id = :id")
    suspend fun deleteBook(id: Int)
    @Update
    suspend fun updateBook(book: Book)
}