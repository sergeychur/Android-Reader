package ru.tp_project.androidreader.model

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.dao.TaskDao
import ru.tp_project.androidreader.model.dao.UserStatisticDao
import ru.tp_project.androidreader.model.data_models.Task
import ru.tp_project.androidreader.model.data_models.User

@Database(entities = [User::class, Task::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun userStatisticDao(): UserStatisticDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        @Synchronized
        fun getInstance(context: Context): AppDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            Log.d("DBcreate", "Db created")
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDb::class.java,
                "app_database"
            ).addCallback(
                object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        db.execSQL(
                            "INSERT INTO user VALUES(?, 0, 0, 0, 0, 0, 0, 0, 0, 0)",
                            intArrayOf(context.resources.getInteger(R.integer.single_user_id)).toTypedArray()
                        )
                        // remove in future
                        db.execSQL("INSERT INTO task VALUES(1, 'task#1', 'vip task', 1, 181881, 21212, 1, 2, 3)")
                    }
                }
            )
                .build()
            INSTANCE = instance
            return instance
        }
    }
}