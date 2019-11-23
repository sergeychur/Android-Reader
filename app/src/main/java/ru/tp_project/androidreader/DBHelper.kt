package ru.tp_project.androidreader

import ru.tp_project.androidreader.dao.UserStatisticDb
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.tp_project.androidreader.dao.UserStatisticDao

object DBHelper {

    private var userStatisticDb: UserStatisticDb? = null

    fun getUserStatisticDb(context: Context): UserStatisticDao {
        if (userStatisticDb == null) {
            userStatisticDb =
                Room.databaseBuilder(context, UserStatisticDb::class.java, "users")
                    .allowMainThreadQueries().addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL(
                                "INSERT INTO user VALUES(1, 0, 0, 1, 0, 1, 0, 0, 0, 1)"
                                //intArrayOf(R.integer.single_user_id).toTypedArray()
                            )
                        }
                    }).build()
        }
        return userStatisticDb!!.userStatisticDao()
    }
}