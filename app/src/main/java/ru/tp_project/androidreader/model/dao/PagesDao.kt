package ru.tp_project.androidreader.model.dao

import androidx.room.*
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.data_models.Pages

@Dao
interface PagesDao {
    @Query("SELECT * FROM pages where bookID = :id")
    suspend fun get(id: Int): Pages

    @Insert
    suspend fun add(pages: Pages)

    @Query("DElETE FROM pages WHERE bookID = :id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(pages: Pages)
}