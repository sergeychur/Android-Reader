package ru.tp_project.androidreader.view.tasks_list

import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.model.data_models.Task
import ru.tp_project.androidreader.model.repos.TasksRepository
import ru.tp_project.androidreader.viewmodel.BaseViewModel


class TasksListViewModel : BaseViewModel() {
    val tasksListLive = MutableLiveData<List<Task>>()

    fun fetchTasksList() {
        dataLoading.value = true
        TasksRepository.getInstance().getTasksList { isSuccess, tasks ->
            dataLoading.value = false
            if (isSuccess) {
                tasksListLive.value = tasks
                empty.value = false
            } else {
                empty.value = true
            }
        }
    }
}