package ru.tp_project.androidreader.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.tp_project.androidreader.model.data_models.User

@Dao
interface UserStatisticDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(user: User)

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun load(userId: Int): User
}