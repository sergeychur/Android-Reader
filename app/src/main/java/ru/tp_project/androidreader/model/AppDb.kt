package ru.tp_project.androidreader.model

import android.content.Context
import androidx.room.*
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.dao.*
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.data_models.Task
import ru.tp_project.androidreader.model.data_models.User
import ru.tp_project.androidreader.model.data_models.*

@Database(
    entities = [User::class, Task::class, BookTaskStat::class,
        Book::class, TaskStatDB::class, TaskBook::class,
        Pages::class], version = 1
)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun userStatisticDao(): UserStatisticDao
    abstract fun taskDao(): TaskDao
    abstract fun bookDao(): BookDao
    abstract fun booksDao(): BooksDao
    abstract fun pagesDao(): PagesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        @Synchronized
        fun getInstance(context: Context): AppDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

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
                    }
                }
            ).build()
            INSTANCE = instance
            return instance
        }
    }
}
