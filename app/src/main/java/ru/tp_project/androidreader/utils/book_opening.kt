package ru.tp_project.androidreader.utils

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.simpleframework.xml.core.Persister
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.ReaderApp
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.data_models.Pages
import ru.tp_project.androidreader.model.xml.BookXML
import ru.tp_project.androidreader.view.BookShelfFragment
import ru.tp_project.androidreader.view.book_viewer.PagesCount
import ru.tp_project.androidreader.view.book_viewer.TextSize
import java.io.InputStream
import java.io.StringReader
import kotlin.math.abs

fun stringSize(raw: String): String {
    var size = raw.length.toFloat()
    var typeId = 0
    var type = ""
    size /= 1024
    while (size > 1024) {
        size /= 1024
        typeId++
    }
    when (typeId) {

        0 -> type = ReaderApp.getInstance().getString(R.string.kb)
        1 -> type = ReaderApp.getInstance().getString(R.string.mb)
        2 -> type = ReaderApp.getInstance().getString(R.string.gb)

    }
    val sizeString = "%.2f".format(size)
    return "$sizeString $type"
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
    val pppp = deleteTags(row)
    return getAttribute("p", row)
}

fun deleteTags(origin : String) : String {
    var work = origin
    while (work.indexOf("<") !=  work.indexOf(">")) {
        val l = work.indexOf("<")
        val r = work.indexOf(">")
        if (l == -1 || r == -1) {
            break
        }
        work = work.substring(0, l) + work.substring(r)
    }
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


fun loadBookXML(origin: String): BookXML? {
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
    Log.d("first:", section)
    while (section.length > 0) {
        Log.d("before:", section)
        var ps = section.split("<p>")
        for (row in ps) {
            var sm = row.split("</p>")
            if (sm.size > 0) {
                rows.add(sm[0])
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

fun loadBookXML3(xml: String): BookXML? {
    val reader = StringReader(xml)
    Log.d("xml: String", xml)
    val serializer = Persister()
    var book: BookXML
    try {
        book = serializer.read(BookXML::class.java, reader, false)
    } catch (e: Exception) {
        return null
    }

    val content = getContent(xml)
    book = addContentToModel(book, content)
    return book
}

private fun getContent(xml: String): String? {
    val start = xml.indexOf("<section>") + "<section>".length
    val end = xml.indexOf("</section>")
    if (start >= end) {
        return null
    }
    return xml.substring(start..end)
}

private fun addContentToModel(book: BookXML, content: String?): BookXML {
    val rows: MutableList<String> = mutableListOf()
    if (content != null) {
        val strings = content.split("</p>")
        val s = 4
        for (str in strings) {
            if (str.length < s) {
                continue
            }
            rows.add(str.substring(s))
        }
    }
    if (rows.size > 0) {
        book.body.section = rows
    } else {
        book.body.section = listOf("No content")
    }
    return book
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

fun createTextSize(content: List<String>, bookID : Int, fragment: Fragment): TextSize {
    val textviewPage = fragment.layoutInflater.inflate(
        R.layout.book_viewer_fragment, null,
        false
    ) as ViewGroup
    val layout = textviewPage.findViewById(R.id.mText) as LinearLayout
    val view = layout.findViewById(R.id.text) as TextView

    val displayMetrics = DisplayMetrics()
    fragment.activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    val width = displayMetrics.widthPixels

    val verticalMargin = fragment.resources.getDimension(R.dimen.activity_vertical_margin)
    val paint = view.paint

    //Working Out How Many Lines Can Be Entered In The Screen
    val fm = paint.fontMetrics
    var textHeight = fm.top - fm.bottom
    textHeight = abs(textHeight)

    val maxLineCount = ((height - verticalMargin) / textHeight).toInt()

    return TextSize (paint, width, maxLineCount, content, bookID)
}

fun launchBook(fragment: Fragment, path: Uri,
                       saveToDBCallback: (context: Context,
                                          book: Book,
                                          pages: Pages,
                                          action : (id: Long) -> Unit
                       ) -> Unit
) {
    val input: InputStream? = ReaderApp.getInstance().contentResolver.openInputStream(path)
    val inputAsString = input!!.bufferedReader().use { it.readText() }
    val size = stringSize(inputAsString)
    val book = loadBookXML(inputAsString)
    if (book == null) {
        val builder = AlertDialog.Builder(fragment.activity)
        builder.setTitle(fragment.getString(R.string.wrong_file_title))
        builder.setMessage(fragment.getString(R.string.wrong_file_message))
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            Toast.makeText(
                fragment.activity,
                android.R.string.yes, Toast.LENGTH_SHORT
            ).show()
        }
        builder.show()
    } else {
        Log.d("tick1", "no bug here")
        val neededPath = FileUtils.getPath(ReaderApp.getInstance(), path)
        if (neededPath == null) {
            Log.d("kek", "failed with getting the path")
        }
        Log.d("tick2", "no bug here")
        val bookBD = xmlToDB(book, neededPath!!, size)
        Log.d("tick3", "no bug here")
        val pc = PagesCount{pages ->
            saveToDBCallback(ReaderApp.getInstance(), bookBD, pages) { id ->
                Log.d("tick5", "no bug here")
                bookBD.id = id.toInt()
                BookShelfFragment.showContent(ReaderApp.getInstance(), bookBD)
            }
        }
        Log.d("tick4", "no bug here")
        pc.execute(createTextSize(book.body.section, 0, fragment = fragment))
        Log.d("tick4.5", "no bug here")
    }
}