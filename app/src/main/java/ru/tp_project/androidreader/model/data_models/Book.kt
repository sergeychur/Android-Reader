package ru.tp_project.androidreader.model.data_models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "book", indices = [Index(value = ["id"])])
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