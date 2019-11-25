package ru.tp_project.androidreader.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import ru.tp_project.androidreader.model.data_models.*


@Dao
interface TaskDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(task: Task): Long

    @Insert
    suspend fun saveStat(taskStatDB: TaskStatDB)

    @Insert
    suspend fun saveTaskBook(taskBook: TaskBook)

    @Transaction
    suspend fun createTask(task: Task, books: List<Book>) {
        val taskId = save(task)
        saveStat(TaskStatDB(taskID = taskId.toInt()))
        for (book in books) {
            saveTaskBook(TaskBook(taskId, book.id.toInt()))
        }
    }

    @Query("SELECT t.id, t.name, t.description, t.user_id, t.created, t.deadline," +
            " t.books, t.pages, t.words, ts.books_read, ts.pages_read, ts.words_read FROM task t JOIN task_stat ts" +
            " ON (ts.task_id=t.id) WHERE t.user_id=:userId and ts.done=:done ORDER BY created DESC")
    suspend fun loadAllTasks(userId: Int, done: Boolean): List<TaskStat>

    @Query("DELETE FROM task WHERE id=:taskId")
    suspend fun deleteTask(taskId: Int)
}