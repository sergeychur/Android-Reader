package ru.tp_project.androidreader.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.tp_project.androidreader.models.Book

@Dao
interface BooksDao {
    @Query("SELECT Count(1) FROM book")
    fun count(): Int
}

@Dao
interface BookDao {
    @Insert(onConflict = REPLACE)
    fun save(book: Book)

    @Query("SELECT * FROM book WHERE id = :bookId")
    fun load(bookId: String): Book
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
                                        "                            \"Лев Николаевич Толстой\", 32.3, \"FB2\", 0.3,\n" +
                                        "                            \"Это какой то текст\")"
                                //intArrayOf(R.integer.single_user_id).toTypedArray()
                            )
                            db.execSQL(
                                "INSERT INTO book VALUES( \"2\", \"КАПИТАНСКАЯ ДОЧКА\", \"no\",\n" +
                                        "                            \"Александр Пушкин\", 18.3, \"FB2\", 0.1,\n" +
                                        "                            \"Отец мой Андрей Петрович Гринев в молодости своей служил при графе Минихе и вышел в отставку премьер-майором в 17.. году. С тех пор жил он в своей Симбирской деревне, где и женился на девице Авдотье Васильевне Ю., дочери бедного тамошнего дворянина. Нас было девять человек детей. Все мои братья и сестры умерли во младенчестве.\")"
                                //intArrayOf(R.integer.single_user_id).toTypedArray()
                            )
                            db.execSQL(
                                "INSERT INTO book VALUES( \"3\", \"Матренин двор\", \"no\",\n" +
                                        "                            \"Александр Исаевич Солженицын\", 32.3, \"FB2\", 0.3,\n" +
                                        "                            \"На сто восемьдесят четвёртом километре от Москвы по ветке, что идёт к Мурому и Казани, ещё с добрых полгода после того все поезда замедляли свой ход почти как бы до ощупи. Пассажиры льнули к стёклам, выходили в тамбур: чинят пути, что ли? из графика вышел?\n" +
                                        "\n" +
                                        "Нет. Пройдя переезд, поезд опять набирал скорость, пассажиры усаживались.\n" +
                                        "\n" +
                                        "Только машинисты знали и помнили, отчего это всё.\n" +
                                        "\n" +
                                        "Да я.\")"
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