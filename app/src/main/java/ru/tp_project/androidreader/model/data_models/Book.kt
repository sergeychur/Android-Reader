package ru.tp_project.androidreader.model.data_models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey private val id: String,
    val name: String,
    val photo: String,
    val author : String,
    val size: Float,
    var format: String,
    val progress: Float,
    val text: String
)