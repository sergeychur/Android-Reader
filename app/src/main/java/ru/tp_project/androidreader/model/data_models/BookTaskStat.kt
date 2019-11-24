package ru.tp_project.androidreader.model.data_models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "book_task_stat",
    foreignKeys = [ForeignKey(entity = Task::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("task_id")),
    ForeignKey(entity = Book::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("book_id"))]
)
data class BookTaskStat (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "task_id")
    val taskID: Int,
    @ColumnInfo(name = "book_id")
    val bookID: Int,
    val pages_read: Int,
    val words_read: Int
)