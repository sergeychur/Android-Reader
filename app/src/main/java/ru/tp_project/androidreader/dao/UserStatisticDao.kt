package ru.tp_project.androidreader.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import ru.tp_project.androidreader.model.data_models.User

@Dao
interface UserStatisticDao {
    @Insert(onConflict = REPLACE)
    fun save(user: User)

    @Query("SELECT * FROM user WHERE id = :userId")
    fun load(userId: Int): LiveData<User>
}