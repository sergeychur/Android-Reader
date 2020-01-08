package ru.tp_project.androidreader.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.atomic.AtomicInteger

open class BaseViewModel : ViewModel() {
    val empty = MutableLiveData<Boolean>().apply { value = false }
    val dataLoading = MutableLiveData<Boolean>().apply { value = false }
    var taskMax = 0
    var taskCounter = AtomicInteger()
    var taskSuccess = true

    // start one task
    fun start() {
        dataLoading.value = true
    }

    // finish one task
    fun finish(isSuccess : Boolean) {
        dataLoading.postValue(false)
        empty.postValue(!isSuccess)
    }


    // start N task
    fun startMultiple(N : Int) {
        dataLoading.value = true
        taskMax = N
        taskCounter.set(0)
        taskSuccess = true
    }

    // finish 1 of N task
    fun finishMultiple(isSuccess : Boolean) {
        if (!isSuccess) {
            taskSuccess = isSuccess
        }
        if (taskCounter.addAndGet(1) == taskMax) {
            dataLoading.postValue(false)
            empty.postValue(!taskSuccess)
        }
    }
}