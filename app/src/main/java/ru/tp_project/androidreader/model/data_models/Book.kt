package ru.tp_project.androidreader.model.data_models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class Book (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val photo: String,
    val author : String,

    val date: String,
    val source: String,
    val genre: String,

    val size: String,
    var format: String,
    val progress: Float,
    val text: String,
    val path: String,

    val pages: Int,
    val currPage: Int
) : java.io.Serializable
