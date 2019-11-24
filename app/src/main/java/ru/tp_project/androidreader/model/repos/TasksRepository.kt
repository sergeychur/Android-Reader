package ru.tp_project.androidreader.model.repos

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.data_models.Task
import ru.tp_project.androidreader.model.data_models.TaskStat

class TasksRepository {

    fun getTasksList(done: Boolean, context: Context, onResult: (isSuccess: Boolean, tasks: List<TaskStat>?) -> Unit) {
        GlobalScope.launch {
            val tasks = withContext(Dispatchers.Default) {
                // TODO(sergeychur): get here done value from the outsides
                AppDb.getInstance(context).taskDao().loadAllTasks(context.resources.getInteger(R.integer.single_user_id), done)
            }
            onResult(tasks.isNotEmpty(), tasks)
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