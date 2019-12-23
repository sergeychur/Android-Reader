package ru.tp_project.androidreader.model.repos

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.data_models.Task
import ru.tp_project.androidreader.model.data_models.TaskStat
import java.sql.Date
import java.util.*

class TasksRepository {

    fun createTask(context: Context, userId: Int, taskName: String, books: List<Book>) {
        GlobalScope.launch {
            val task = Task(
                name = taskName,
                userID = userId,
                books = books.size,
                created = Date(Calendar.getInstance().time.time)
            )
            withContext(Dispatchers.Default) {
                AppDb.getInstance(context).taskDao().createTask(task, books)
            }
        }
    }

    fun getTasksList(done: Boolean, context: Context, onResult: (isSuccess: Boolean, tasks: List<TaskStat>?) -> Unit) {
        GlobalScope.launch {
            val tasks = withContext(Dispatchers.Default) {
                AppDb.getInstance(context).taskDao()
                .loadAllTasks(context.resources.getInteger(R.integer.single_user_id), done)
            }
            onResult(tasks.isNotEmpty(), tasks)
        }
    }

    fun deleteTask(taskId: Int, context: Context, onResult: (isSuccess: Boolean) -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                AppDb.getInstance(context).taskDao().deleteTask(taskId)
            }
            onResult(true)
        }
    }

    @Suppress("unused")
    fun getTask(taskId: Int, context: Context, onResult: (isSuccess: Boolean, task: TaskStat) -> Unit) {
        GlobalScope.launch {
            val task = withContext(Dispatchers.Default) {
                AppDb.getInstance(context).taskDao()
                    .getTask(taskId)
            }
            onResult(true, task)
        }
    }

    companion object {
        private var INSTANCE: TasksRepository? = null
        fun getInstance() = INSTANCE
            ?: TasksRepository().also {
                INSTANCE = it
            }
    }
}