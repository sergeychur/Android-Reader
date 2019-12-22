package ru.tp_project.androidreader.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.tp_project.androidreader.model.data_models.Book

open class BaseViewModel : ViewModel() {
    val empty = MutableLiveData<Boolean>().apply { value = false }
    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    fun start() {
        dataLoading.value = true
    }

    fun finish(isSuccess : Boolean) {
        dataLoading.postValue(false)
        if (isSuccess) {
            empty.postValue(false)
        } else {
            empty.postValue(true)
        }
    }
}