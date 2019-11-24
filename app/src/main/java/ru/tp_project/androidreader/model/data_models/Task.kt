package ru.tp_project.androidreader.model.data_models

import androidx.room.*
import java.sql.Date

@Entity(tableName = "task",
    foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id"))],
    indices = [Index(value = ["user_id"])]
)
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    @ColumnInfo(name = "user_id")
    val userID: Int,
    @ColumnInfo(name = "created")
    val created: Date,
    val deadline: Date,
    val books: Int,
    val pages: Int,
    val words: Int
)
