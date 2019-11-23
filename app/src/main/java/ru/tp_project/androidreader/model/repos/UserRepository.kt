package ru.tp_project.androidreader.model.repos

import android.content.Context
import androidx.lifecycle.LiveData
import ru.tp_project.androidreader.DBHelper
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.User

class UserRepository {
    private var currentUserID = R.integer.single_user_id

    fun getUserStatistic(context: Context): LiveData<User> {
        return DBHelper.getUserStatisticDb(context).load(context.resources.getInteger(currentUserID))
    }
}