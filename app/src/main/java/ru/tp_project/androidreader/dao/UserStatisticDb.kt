package ru.tp_project.androidreader.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.tp_project.androidreader.model.data_models.User

@Database(entities = [User::class], version = 1)
abstract class UserStatisticDb : RoomDatabase() {
    abstract fun userStatisticDao(): UserStatisticDao
}