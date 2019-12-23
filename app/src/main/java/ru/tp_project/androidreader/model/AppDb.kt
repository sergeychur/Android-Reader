package ru.tp_project.androidreader.model

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.dao.TaskDao
import ru.tp_project.androidreader.model.dao.UserStatisticDao
import ru.tp_project.androidreader.model.data_models.Book
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
                            "INSERT INTO user VALUES(?, 0, 0, 0, 0, 0, 0, 0, 0, 1)",
                            intArrayOf(context.resources.getInteger(R.integer.single_user_id)).toTypedArray()
                        )
                        // remove in future
                        db.execSQL("INSERT INTO task VALUES(1, 'task#1', 'vip task', 1, 181881, 21212, 1, 2, 3)")
                    }
                }
            ).build()
            INSTANCE = instance
            return instance
        }
    }
}


@Dao
interface BooksDao {
    @Query("SELECT * FROM book")
    suspend fun getAll(): List<Book>
    @Insert
    suspend fun addBook(book: Book)

    @Query("DElETE FROM book WHERE id = :id")
    suspend fun deleteBook(id: Int)
    @Update
    suspend fun updateBook(book: Book)
}

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(book: Book)

    @Query("SELECT * FROM book WHERE id = :bookId")
    fun load(bookId: String): Book
}

@Database(entities = [Book::class], version = 1)
abstract class BookDb : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun booksDao(): BooksDao

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
                                "INSERT INTO book VALUES( 1, \"Война и мир\", \"no\",\n" +
                                        "                            \"Лев Николаевич Толстой\", " +
                                        "\"15.12.2012\", " + "\"kotlin\", " + "\"android\", " +
                                        "\"23.3kb\", " + "\"FB2\", 0.3," +
                                        "\"nopath\",  \"Это какой то текст\", 1, 8)"
                                //intArrayOf(R.integer.single_user_id).toTypedArray()
                            )
//                            db.execSQL(
//                                "INSERT INTO book VALUES( 2, \"КАПИТАНСКАЯ ДОЧКА\", \"no\",\n" +
//                                        "                            \"Александр Пушкин\", 18.3, \"FB2\", 0.1,\n" +
//                                        "                            \"Отец мой Андрей Петрович Гринев в молодости своей служил при графе Минихе и вышел в отставку премьер-майором в 17.. году. С тех пор жил он в своей Симбирской деревне, где и женился на девице Авдотье Васильевне Ю., дочери бедного тамошнего дворянина. Нас было девять человек детей. Все мои братья и сестры умерли во младенчестве.\")"
//                                //intArrayOf(R.integer.single_user_id).toTypedArray()
//                            )
//                            db.execSQL(
//                                "INSERT INTO book VALUES( 3, \"Матренин двор\", \"no\",\n" +
//                                        "                            \"Александр Исаевич Солженицын\", 32.3, \"FB2\", 0.3,\n" +
//                                        "                            \"На сто восемьдесят четвёртом километре от Москвы по ветке, что идёт к Мурому и Казани, ещё с добрых полгода после того все поезда замедляли свой ход почти как бы до ощупи. Пассажиры льнули к стёклам, выходили в тамбур: чинят пути, что ли? из графика вышел?\n" +
//                                        "\n" +
//                                        "Нет. Пройдя переезд, поезд опять набирал скорость, пассажиры усаживались.\n" +
//                                        "\n" +
//                                        "Только машинисты знали и помнили, отчего это всё.\n" +
//                                        "\n" +
//                                        "Да я.\")"
//                                //intArrayOf(R.integer.single_user_id).toTypedArray()
//                            )
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