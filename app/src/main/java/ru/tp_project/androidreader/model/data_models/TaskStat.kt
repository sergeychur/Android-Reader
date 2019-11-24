package ru.tp_project.androidreader.model.data_models

import androidx.room.Embedded

data class TaskStat(
    @Embedded
    val task: Task,
    val booksRead: Int,
    val pagesRead: Int,
    val wordsRead: Int
)