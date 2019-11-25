package ru.tp_project.androidreader.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.tp_project.androidreader.model.data_models.Book

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(book: Book)

    @Query("SELECT * FROM book WHERE id = :bookId")
    fun load(bookId: String): Book
}
