package ru.tp_project.androidreader.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.tp_project.androidreader.model.dao.UserStatisticDao
import ru.tp_project.androidreader.model.data_models.User

@Database(entities = [User::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun userStatisticDao(): UserStatisticDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(AppDb::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "app_database"
                ).addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL(
                                "INSERT INTO user VALUES(1, 0, 0, 1, 0, 1, 0, 0, 0, 1)"
                                //intArrayOf(R.integer.single_user_id).toTypedArray()
                            )
                        }
                    }
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}