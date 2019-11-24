package ru.tp_project.androidreader.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class Book(
    @PrimaryKey val id: String,
    val name: String,
    val photo: String,
    val author : String,
    val size: Float,
    var format: String,
    val progress: Float,
    val text: String
)