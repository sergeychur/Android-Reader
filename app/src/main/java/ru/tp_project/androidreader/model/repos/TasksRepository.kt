package ru.tp_project.androidreader.model.repos

import ru.tp_project.androidreader.model.data_models.Task

class TasksRepository {

    fun getTasksList(onResult: (isSuccess: Boolean, tasks: List<Task>?) -> Unit) {
        // TODO(me): change implementation
        val Tasks: List<Task> = listOf(Task("5"), Task("6"), Task("7"))
        onResult(true, Tasks)
    }

    companion object {
        private var INSTANCE: TasksRepository? = null
        fun getInstance() = INSTANCE
            ?: TasksRepository().also {
                INSTANCE = it
            }
    }
}