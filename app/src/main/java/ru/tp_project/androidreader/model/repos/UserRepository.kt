package ru.tp_project.androidreader.model.repos

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.data_models.User

class UserRepository {
    private var currentUserID = R.integer.single_user_id

    fun getUserStatistic(context: Context, onResult: (isSuccess: Boolean, user: User?) -> Unit) {
        GlobalScope.launch {
            val user = withContext(Dispatchers.Default) {
                AppDb.getInstance(context).userStatisticDao().load(context.resources.getInteger(currentUserID))
            }
            onResult(true, user)
        }

    }

}