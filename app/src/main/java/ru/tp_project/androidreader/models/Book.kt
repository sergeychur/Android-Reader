package ru.tp_project.androidreader.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey private val id: String,
    public val name: String,
    val photo: String,
    val author : String,
    val size: Float,
    var format: String,
    val progress: Float,
    val text: String
)