package ru.tp_project.androidreader.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.User
import ru.tp_project.androidreader.model.repos.UserRepository

class CommonStatsViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepository = UserRepository()
    private var context = application.applicationContext
    var statistic = MutableLiveData<User>()
    private var fail = MutableLiveData<Boolean>().apply { value = false }

    fun getStatistic() {
        userRepository.getUserStatistic(context) { isSuccess, user ->
            if (isSuccess) {
                statistic.postValue(user)
            } else {
                fail.postValue(true)
            }
        }
    }

    fun getStatisticText(): String {
        val stat = statistic.value
        return context.getString(R.string.share_text).format(
            stat!!.booksRead, stat.pagesRead, stat.wordsRead,
            stat.hoursPerDay, stat.wordsPerMin, stat.pagesPerHour,
            stat.years, stat.days, stat.hours
        )
    }
}