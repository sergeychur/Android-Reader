package ru.tp_project.androidreader.model.repos

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.model.AppDb
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.xml.BookXML
import java.io.File
import java.io.InputStream
import android.text.Html



class BookRepositoryFS {
    fun getBookFB2FromFile(path: String): BookXML? {
        val inputStream: InputStream = File(path).inputStream()
        val xml = inputStream.bufferedReader().use { it.readText() }
        return getBookFB2(xml)
    }
    fun getBookFB2(origin: String): BookXML? {
        var xml = origin
        var bookXML = BookXML()

        // description {
        // titleInfo {
        bookXML.description.titleInfo.genre = getAttribute("genre", xml)
        Log.d("itleInfo.genre", bookXML.description.titleInfo.genre)
        bookXML.description.titleInfo.author.first_name = getAttribute("first-name", xml)
        Log.d("author.first_name", bookXML.description.titleInfo.author.first_name)
        bookXML.description.titleInfo.author.last_name = getAttribute("last-name", xml)
        Log.d("author.last_name", bookXML.description.titleInfo.author.last_name)
        bookXML.description.titleInfo.book_title = getAttribute("book-title", xml)
        Log.d("itleInfo.book_title", bookXML.description.titleInfo.book_title)
        bookXML.description.titleInfo.lang = getAttribute("lang", xml)
        Log.d("itleInfo.lang", bookXML.description.titleInfo.lang)
        xml = cut("title-info", xml)
        // }

        // documentInfo {
        bookXML.description.documentInfo.author.nickname = getAttribute("nickname", xml)
        Log.d("author.nickname", bookXML.description.documentInfo.author.nickname)
        bookXML.description.documentInfo.author.first_name = getAttribute("first-name", xml)
        Log.d("author.first_name", bookXML.description.documentInfo.author.first_name)
        bookXML.description.documentInfo.author.last_name = getAttribute("last-name", xml)
        Log.d("author.last_name", bookXML.description.documentInfo.author.last_name)
        bookXML.description.documentInfo.author.fixNames()
        bookXML.description.documentInfo.date = getAttribute("date", xml)
        Log.d("itleInfo.date", bookXML.description.documentInfo.date)
        bookXML.description.documentInfo.version = getAttribute("version", xml)
        Log.d("itleInfo.version", bookXML.description.documentInfo.version)
        // }
        xml = cut("description", xml)
        // }

        bookXML.binary = getAttribute("binary", xml)

        // body
        val rows: MutableList<String> = mutableListOf()
        var section = getAttribute("section", xml)
        while (section.length > 0) {
            var ps = section.split("<p>")
            for (row in ps) {
                var sm = row.split("</p>")
                if (sm.size > 0) {
                    rows.add( deleteTags(sm[0]))
                }
            }

            bookXML.body.section = rows
            //Log.d("beforecut:", section)
            xml = cut("section", xml)
            //Log.d("aftercut:", section)
            section = getAttribute("section", xml)
            //Log.d("xml: section:", section)
        }
        Log.d("bookXML done!:", "lol")
        return bookXML
    }

    fun xmlToDB(bookXML: BookXML, path: String, size: String): Book {
        return Book(
            0, bookXML.description.titleInfo.book_title,
            bookXML.binary, bookXML.description.titleInfo.author.first_name +
                    bookXML.description.titleInfo.author.last_name,
            bookXML.description.titleInfo.date,
            bookXML.description.publishInfo.publisher,
            bookXML.description.titleInfo.genre,
            size, "fb2", 0f, path, 0, 0, 0
        )
    }


    fun cut(name : String, row : String) : String {
        val e = row.indexOf("</"+name+ ">")+("</"+name+">").length
        if (e >= 0) {
            return row.substring(e)
        }
        return row
    }

    fun getP(row : String) : String {
        val ppp = getAttribute("p", row)
        val pppp = deleteTags(ppp)
        return pppp
    }

    fun deleteTags(origin : String) : String {
        var work = origin
        return Html.fromHtml(origin).toString()
//        tags = work.split("<")
//        while

//        while (work.indexOf("<") !=  work.indexOf(">")) {
//            val l = work.indexOf("<")
//            val r = work.indexOf(">")
//            if (l == -1 || r == -1) {
//                break
//            }
//            work = work.substring(0, l) + work.substring(r)
//        }
        return work
    }

    fun getAttribute(name : String, row : String) : String {
        val l = row.indexOf("<"+name)
        //Log.d("we l","<"+name+" " + l + " !")
        if (l < 0) {
            return ""
        }
        //Log.d("return ",row.substring(l,l+20))
        val r = row.substring(l).indexOf(">")+l+1
        //Log.d("we r","> " + l + " !")
        if (r-l-1< 0) {
            return ""
        }
        //Log.d("return ",row.substring(r,r+10))
        val e = row.substring(r).indexOf("</"+name)+r
        //Log.d("we e","</"+name + " " + e + " !")
        if (e-r < 0) {
            return ""
        }
        //Log.d("return e",row.substring(e,e+10))
        //Log.d("return",row.substring(r,e))
        return row.substring(r,e)
    }
}