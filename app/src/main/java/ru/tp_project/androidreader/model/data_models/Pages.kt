package ru.tp_project.androidreader.model.data_models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters(PagesConverters::class)
@Entity(tableName = "pages")
data class Pages (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    var bookID : Int,
    // индекс в массиве строк и индекс символа в строке
    var pageStart : ArrayList<Pair<Int, Int>>,
    var pageEnd : ArrayList<Pair<Int, Int>>,

    var pageWordsSymbols : ArrayList<Pair<Int, Int>>,
    var width : Int,
    var maxLines : Int,
    var pageCurrent : Int,
    var pageCount : Int
    //var times : ArrayList<Long>
) : java.io.Serializable

class PagesConverters {
    @TypeConverter
    fun fromArrayList(me: ArrayList<Pair<Int, Int>>): String = me.joinToString()

    @TypeConverter
    fun fromString(str: String) : ArrayList<Pair<Int, Int>> {
        val arr = ArrayList<Pair<Int, Int>>()
        val s =  str.split(")")
        for (a in s) {
            val b = a.removePrefix(", (").removePrefix("(").split(", ")
            if (b.size == 2) {
                arr.add(Pair(b[0].toInt(),b[1].toInt()))
            }

        }
        return arr
    }
}