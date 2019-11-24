package ru.tp_project.androidreader.model.repos

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.data_models.Task

class TasksRepository {

    fun getTasksList(context: Context, onResult: (isSuccess: Boolean, tasks: List<Task>?) -> Unit) {
        GlobalScope.launch {
            val tasks = withContext(Dispatchers.Default) {
                AppDb.getInstance(context).taskDao().loadAllTasks(context.resources.getInteger(R.integer.single_user_id))
            }
            onResult(true, tasks)
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