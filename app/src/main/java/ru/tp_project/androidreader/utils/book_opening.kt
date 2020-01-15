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
import ru.tp_project.androidreader.model.repos.BookRepositoryFS
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
    val bookLoader = BookRepositoryFS()
    val book = bookLoader.getBookFB2(inputAsString)
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
        val bookBD = bookLoader.xmlToDB(book, neededPath!!, size)
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