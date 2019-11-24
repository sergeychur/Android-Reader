package ru.tp_project.androidreader.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.tp_project.androidreader.model.data_models.Task


@Dao
interface TaskDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(task: Task)

    @Query("SELECT * FROM task WHERE id = :userId ORDER BY created DESC")
    suspend fun load(userId: Int): List<Task>
}