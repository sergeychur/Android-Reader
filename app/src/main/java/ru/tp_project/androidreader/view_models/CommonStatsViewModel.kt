package ru.tp_project.androidreader.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.tp_project.androidreader.model.data_models.User
import ru.tp_project.androidreader.model.repos.UserRepository

class CommonStatsViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepository = UserRepository()
    private var statistic = userRepository.getUserStatistic(application.applicationContext)

    fun getStatistic(): LiveData<User> {
        return statistic
    }
}