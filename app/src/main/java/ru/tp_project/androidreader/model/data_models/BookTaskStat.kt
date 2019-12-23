package ru.tp_project.androidreader.model.data_models

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "book_task_stat",
    foreignKeys = [ForeignKey(onDelete = CASCADE, entity = Task::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("task_id")),
    ForeignKey(onDelete = CASCADE, entity = Book::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("book_id"))],
    indices = [Index(value = ["task_id"]),
        Index(value = ["book_id"])]
)
data class BookTaskStat (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "task_id")
    val taskID: Int,
    @ColumnInfo(name = "book_id")
    val bookID: Int,
    @ColumnInfo(name = "pages_read")
    val pagesRead: Int,
    @ColumnInfo(name = "words_read")
    val wordsRead: Int
)