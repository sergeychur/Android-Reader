package ru.tp_project.androidreader.model.data_models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    primaryKeys = ["taskId", "bookId"],
    foreignKeys = [
        ForeignKey(
            onDelete = CASCADE, entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"]
        ),
        ForeignKey(
            onDelete = CASCADE, entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"]
        )
    ]
)
data class TaskBook(
    val taskId: Long,
    val bookId: Int
)