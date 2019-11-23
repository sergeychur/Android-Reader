package ru.tp_project.androidreader.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.tp_project.androidreader.models.User
import ru.tp_project.androidreader.repositories.UserRepository

class CommonStatsViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepository = UserRepository()
    private var statistic = userRepository.getUserStatistic(application.applicationContext)

    fun getStatistic(): LiveData<User> {
        return statistic
    }
}