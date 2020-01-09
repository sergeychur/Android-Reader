package ru.tp_project.androidreader.model.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import ru.tp_project.androidreader.model.data_models.*


@Dao
interface TaskDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(task: Task): Long

    @Insert
    suspend fun saveStat(taskStatDB: TaskStatDB): Long

    @Insert
    suspend fun saveTaskBook(taskBook: TaskBook)

    @Transaction
    suspend fun createTask(task: Task, books: List<Book>) {
        val taskId = save(task)
        val taskStatId = saveStat(TaskStatDB(taskID = taskId.toInt()))

        for (book in books) {
            saveTaskBook(TaskBook(taskId, book.id))
        }

        countTaskStat(TaskStatDB(taskStatId.toInt(), taskId.toInt()))
    }

    @Query(
        "SELECT t.id, t.name, t.description, t.user_id, t.created, t.deadline," +
                " t.books, t.pages, t.words, ts.books_read, ts.pages_read, ts.words_read FROM task t JOIN task_stat ts" +
                " ON (ts.task_id=t.id) WHERE t.user_id=:userId and ts.done=:done ORDER BY created DESC"
    )
    suspend fun loadAllTasks(userId: Int, done: Boolean): List<TaskStat>

    @Query("DELETE FROM task WHERE id=:taskId")
    suspend fun deleteTask(taskId: Int)

    @Query(
        "SELECT t.id, t.name, t.description, t.user_id, t.created, t.deadline," +
                " t.books, t.pages, t.words, ts.books_read, ts.pages_read, ts.words_read FROM task t JOIN task_stat ts" +
                " ON (ts.task_id=t.id) WHERE t.id=:taskId"
    )
    suspend fun getTask(taskId: Int): TaskStat

    @Query("SELECT * FROM task_stat JOIN TaskBook ON taskId = task_id where bookId = :bookId")
    suspend fun getBookTasks(bookId: Int): List<TaskStatDB>

    @Query("SELECT * from book JOIN TaskBook on bookId = book.id WHERE taskId = :taskId")
    suspend fun getTaskBooks(taskId: Int): List<Book>

    @Update
    suspend fun updateTaskStat(taskStatDB: TaskStatDB)

    @Transaction
    suspend fun recountTasksStats(bookId: Int) {
        val tasks = getBookTasks(bookId)
        for (task in tasks) {
            countTaskStat(task)
        }
    }

    @Query("SELECT * FROM pages where bookID = :id")
    suspend fun getBookPages(id: Int): Pages

    @Transaction
    suspend fun countTaskStat(taskStatDB: TaskStatDB) {
        val books = getTaskBooks(taskStatDB.taskID)

        var booksRead = 0
        var pagesRead = 0
        var wordsRead = 0L

        for (book in books) {
            val pages = getBookPages(book.id)
            pagesRead += pages.pageCurrent + 1
            wordsRead += pages.pageWordsSymbols.slice(0..pages.pageCurrent).sumBy { it.first }
            if (pages.pageCurrent + 1 == pages.pageCount) {
                booksRead += 1
            }
        }

        val done = (booksRead == books.size)
        updateTaskStat(
            taskStatDB.copy(
                booksRead = booksRead,
                pagesRead = pagesRead,
                wordsRead = wordsRead,
                done = done
            )
        )
    }
}