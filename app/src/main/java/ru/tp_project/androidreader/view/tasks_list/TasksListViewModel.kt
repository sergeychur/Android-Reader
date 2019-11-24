package ru.tp_project.androidreader.view.tasks_list

import android.content.Context
import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.model.data_models.Task
import ru.tp_project.androidreader.model.repos.TasksRepository
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.TaskStat


class TasksListViewModel : BaseViewModel() {
    val tasksListLive = MutableLiveData<MutableList<TaskStat>>()

    fun clearTasksList() {
        tasksListLive.value?.removeAll {true}
    }
    fun fetchTasksList(context: Context, done: Boolean) {
        dataLoading.value = true
        TasksRepository.getInstance().getTasksList(done, context) { isSuccess, tasks ->
            dataLoading.postValue(false)
            if (isSuccess) {
                tasksListLive.postValue(tasks?.toMutableList())
                empty.postValue(false)
            } else {
                empty.postValue(true)
            }
        }
    }
}