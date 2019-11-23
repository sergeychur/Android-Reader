package ru.tp_project.androidreader.model.repos

import android.content.Context
import androidx.lifecycle.LiveData
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.data_models.User

class UserRepository {
    private var currentUserID = R.integer.single_user_id

    fun getUserStatistic(context: Context): LiveData<User> {
        return AppDb.getInstance(context).userStatisticDao()
            .load(context.resources.getInteger(currentUserID))
    }
}