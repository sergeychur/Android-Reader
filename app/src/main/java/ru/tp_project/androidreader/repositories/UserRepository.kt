package ru.tp_project.androidreader.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import ru.tp_project.androidreader.DBHelper
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.models.User

class UserRepository {
    private var currentUserID = R.integer.single_user_id

    fun getUserStatistic(context: Context): LiveData<User> {
        return DBHelper.getUserStatisticDb(context).load(context.resources.getInteger(currentUserID))
    }
}