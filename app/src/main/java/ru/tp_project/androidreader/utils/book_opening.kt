package ru.tp_project.androidreader.utils

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.simpleframework.xml.core.Persister
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.ReaderApp
import ru.tp_project.androidreader.base.BaseViewModel
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

fun loadBookXML(xml: String): BookXML? {
    val reader = StringReader(xml)
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
    var str = ""
    for (s in bookXML.body.section) {
        str += s
    }
    return Book(
        0, bookXML.description.titleInfo.book_title,
        bookXML.binary, bookXML.description.titleInfo.author.first_name +
                bookXML.description.titleInfo.author.last_name,
        bookXML.description.titleInfo.date,
        bookXML.description.publishInfo.publisher,
        bookXML.description.titleInfo.genre,
        size, "fb2", 0f, str, path, 0, 0, 0
    )
}

fun createTextSize(content: String, bookID : Int, fragment: Fragment): TextSize {
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
        val bookBD = xmlToDB(book, path.path!!, size)
        val pc = PagesCount{pages ->
            saveToDBCallback(ReaderApp.getInstance(), bookBD, pages) { id ->
                bookBD.id = id.toInt()
                BookShelfFragment.showContent(ReaderApp.getInstance(), bookBD)
            }
        }
        pc.execute(createTextSize(book.body.section.joinToString(""), 0, fragment = fragment))
    }
}