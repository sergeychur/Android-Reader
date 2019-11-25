package ru.tp_project.androidreader.view.tasks_list

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import ru.tp_project.androidreader.model.repos.TasksRepository
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.TaskStat


class TasksListViewModel : BaseViewModel() {
    val tasksListLive = MutableLiveData<MutableList<TaskStat>>()
    private val tabNumber = MutableLiveData<Int>()
    val changed = MutableLiveData<Boolean>()
    fun clearTasksList() {
        tasksListLive.value?.clear()
    }
    fun isActive(): Boolean? {
        return tabNumber.value == null || tabNumber.value == 0
    }

    fun setTabNum(num: Int?) {
        tabNumber.value = num ?: 0
    }
    fun fetchTasksList(context: Context, done: Boolean?) {
        dataLoading.postValue(true)
        val doneVal = done ?: false
        TasksRepository.getInstance().getTasksList(doneVal, context) { isSuccess, tasks ->
            dataLoading.postValue(false)
            if (isSuccess) {
                tasksListLive.postValue(tasks?.toMutableList())
                empty.postValue(false)
            } else {
                empty.postValue(true)
            }
        }
    }
    fun deleteTask(context: Context, taskId: Int, successCallback: ()-> Unit) {
        Log.d("kek", "deleted")
        TasksRepository.getInstance().deleteTask(taskId, context) {isSuccess ->
            if (isSuccess) {
                tasksListLive.value?.removeAll { taskStat -> taskStat.id == taskId }
                successCallback()
                val changedVal = changed.value ?: false
                changed.postValue(!changedVal)
            }
        }
    }
}