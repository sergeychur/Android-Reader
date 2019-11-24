package ru.tp_project.androidreader.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.tp_project.androidreader.model.data_models.Task
import ru.tp_project.androidreader.model.data_models.TaskStat


@Dao
interface TaskDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(task: Task)

    @Query("SELECT COUNT(*), SUM() FROM task t JOIN book_task_stat bs ON (t.id == bs.task_id) WHERE user_id = :userId ORDER BY created DESC")
    suspend fun loadAllTasks(userId: Int): List<TaskStat>
}