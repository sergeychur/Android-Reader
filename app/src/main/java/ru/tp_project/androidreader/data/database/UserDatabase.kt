package ru.tp_project.androidreader.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.tp_project.androidreader.models.Book

@Dao
interface BookDao {
    @Insert(onConflict = REPLACE)
    fun save(book: Book)

    @Query("SELECT * FROM book WHERE id = :userId")
    fun load(userId: String): Book
}

@Database(entities = [Book::class], version = 1)
abstract class BookDb : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDb? = null

        fun getInstance(context: Context): BookDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(BookDb::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDb::class.java,
                    "book_database"
                ).addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL(
                                "INSERT INTO book VALUES( \"1\", \"Война и мир\", \"no\",\n" +
                                        "                            \"Лев Николаевич Толстой\", 32.3f, \"FB2\", 0.3f,\n" +
                                        "                            \"Это какой то текст\")"
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