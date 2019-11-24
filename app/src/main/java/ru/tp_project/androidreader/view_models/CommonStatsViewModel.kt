package ru.tp_project.androidreader.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.User
import ru.tp_project.androidreader.model.repos.UserRepository

class CommonStatsViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepository = UserRepository()
    private var context = application.applicationContext
    var statistic = MutableLiveData<User>()
    var fail = MutableLiveData<Boolean>().apply { value = false }

    fun getStatistic() {
        userRepository.getUserStatistic(context) { isSuccess, user ->
            if (isSuccess) {
                statistic.postValue(user)
            } else {
                fail.postValue(true)
            }
        }
    }
}