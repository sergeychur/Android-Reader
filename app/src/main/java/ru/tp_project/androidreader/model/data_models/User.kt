package ru.tp_project.androidreader.model.data_models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @ColumnInfo(name = "id")
    @PrimaryKey val id: Int,
    val booksRead: Int,
    val pagesRead: Int,
    val wordsRead: Int,
    val hoursPerDay: Int,
    val wordsPerMin: Int,
    val pagesPerHour: Int,
    val years: Int,
    val days: Int,
    val hours: Int
)