package ru.tp_project.androidreader.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val empty = MutableLiveData<Boolean>().apply { value = false }
    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

}