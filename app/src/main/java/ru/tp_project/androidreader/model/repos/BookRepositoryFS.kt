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
        bookXML.description.titleInfo.genre = getAttributeNoTags("genre", xml)
        if (bookXML.description.titleInfo.genre == "") {
            bookXML.description.titleInfo.genre = getAttributeNoTags("annotation", xml)
        }
        Log.d("itleInfo.genre", bookXML.description.titleInfo.genre)
        bookXML.description.titleInfo.author.first_name = getAttributeNoTags("first-name", xml)
        Log.d("author.first_name", bookXML.description.titleInfo.author.first_name)
        bookXML.description.titleInfo.author.last_name = getAttributeNoTags("last-name", xml)
        Log.d("author.last_name", bookXML.description.titleInfo.author.last_name)
        bookXML.description.titleInfo.book_title = getAttributeNoTags("book-title", xml)
        Log.d("itleInfo.book_title", bookXML.description.titleInfo.book_title)
        bookXML.description.titleInfo.lang = getAttributeNoTags("lang", xml)
        Log.d("itleInfo.lang", bookXML.description.titleInfo.lang)
        xml = cut("title-info", xml)
        // }

        // documentInfo {
        bookXML.description.documentInfo.author.nickname = getAttributeNoTags("nickname", xml)
        Log.d("author.nickname", bookXML.description.documentInfo.author.nickname)
        bookXML.description.documentInfo.author.first_name = getAttributeNoTags("first-name", xml)
        Log.d("author.first_name", bookXML.description.documentInfo.author.first_name)
        bookXML.description.documentInfo.author.last_name = getAttributeNoTags("last-name", xml)
        Log.d("author.last_name", bookXML.description.documentInfo.author.last_name)
        bookXML.description.documentInfo.author.fixNames()
        bookXML.description.documentInfo.date = getAttributeNoTags("date", xml)
        Log.d("itleInfo.date", bookXML.description.documentInfo.date)
        bookXML.description.documentInfo.version = getAttributeNoTags("version", xml)
        Log.d("itleInfo.version", bookXML.description.documentInfo.version)

        bookXML.description.publishInfo.publisher = getAttributeNoTags("publisher", xml)
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
        var author = bookXML.description.titleInfo.author.first_name+bookXML.description.titleInfo.author.last_name
        val nickname = bookXML.description.documentInfo.author.first_name+bookXML.description.documentInfo.author.last_name
        if (author != "" && nickname != "") {
            author += "/"
        }
        author += nickname
        Log.d("look at author", author)

        return Book(
            0, bookXML.description.titleInfo.book_title,
            bookXML.binary, author,
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
        return Html.fromHtml(origin).toString()
    }

    fun getAttribute(name : String, row : String) : String {
        val l = row.indexOf("<"+name)
        if (l < 0) {
            return ""
        }
        val r = row.substring(l).indexOf(">")+l+1
        if (r-l-1< 0) {
            return ""
        }
        val e = row.substring(r).indexOf("</"+name)+r
        if (e-r < 0) {
            return ""
        }
        return row.substring(r,e)
    }

    fun getAttributeNoTags(name : String, row : String) : String {
        val l = row.indexOf("<"+name)
        if (l < 0) {
            return ""
        }
        val r = row.substring(l).indexOf(">")+l+1
        if (r-l-1< 0) {
            return ""
        }
        val e = row.substring(r).indexOf("</"+name)+r
        if (e-r < 0) {
            return ""
        }
        return deleteTags(row.substring(r,e))
    }
}