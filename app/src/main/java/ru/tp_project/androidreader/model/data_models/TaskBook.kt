package ru.tp_project.androidreader.model.data_models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["taskId", "bookId"],
    foreignKeys = [
        ForeignKey(entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"]),
        ForeignKey(entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"])
    ])
data class TaskBook(
    val taskId: Long,
    val bookId: Int
)