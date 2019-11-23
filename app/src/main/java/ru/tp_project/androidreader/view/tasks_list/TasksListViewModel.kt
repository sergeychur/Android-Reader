package ru.tp_project.androidreader.view.tasks_list

import android.content.Context
import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.model.data_models.Task
import ru.tp_project.androidreader.model.repos.TasksRepository
import ru.tp_project.androidreader.base.BaseViewModel


class TasksListViewModel : BaseViewModel() {
    val tasksListLive = MutableLiveData<List<Task>>()

    fun fetchTasksList(context: Context) {
        dataLoading.value = true
        TasksRepository.getInstance().getTasksList(context) { isSuccess, tasks ->
            dataLoading.postValue(false)
            if (isSuccess) {
                tasksListLive.postValue(tasks)
                empty.postValue(false)
            } else {
                empty.postValue(true)
            }
        }
    }
}