package ru.tp_project.androidreader.model.data_models

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.sql.Date

data class TaskStat(
    val id: Int,
    val name: String,
    val description: String,
    @ColumnInfo(name = "user_id")
    val userID: Int,
    val created: Date,
    val deadline: Date,
    val books: Int,
    val pages: Int,
    val words: Int,
    @ColumnInfo(name = "books_read")
    val booksRead: Int,
    @ColumnInfo(name = "pages_read")
    val pagesRead: Int,
    @ColumnInfo(name = "words_read")
    val wordsRead: Int
)

@Entity(
    tableName = "task_stat",
    foreignKeys = [ForeignKey(
        onDelete = CASCADE, entity = Task::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("task_id")
    )],
    indices = [Index(value = ["task_id"])]
)
data class TaskStatDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "task_id")
    val taskID: Int,
    @ColumnInfo(name = "books_read")
    val booksRead: Int = 0,
    @ColumnInfo(name = "pages_read")
    val pagesRead: Int = 0,
    @ColumnInfo(name = "words_read")
    val wordsRead: Int = 0,
    val done: Boolean = false
)