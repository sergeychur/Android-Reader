package ru.tp_project.androidreader.view.book_viewer

import android.os.AsyncTask
import android.text.TextPaint
import android.util.Log
import androidx.viewpager.widget.ViewPager
import ru.tp_project.androidreader.model.data_models.Pages


class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}

class TextSize(
    var paint: TextPaint,
    var screenWidth: Int,
    var lineMax: Int,
    var content: String,
    var bookID: Int
)

class PagesCount(val finish: (pages: Pages) -> Unit) {
    fun execute(ts: TextSize) {
        doAsync {
            count(ts)
        }.execute()
    }

    fun count(ts: TextSize){

        var pages = Pages(0,ts.bookID, ArrayList(),
            ArrayList(),ts.screenWidth, ts.lineMax,0, 0)

        var lineCount = 0
        var content = ts.content
        addpage(pages, 0, 0, "") // добавляем обложку
        var symbolsCount = 0
        var pageContent = ""

            while (content.isNotEmpty()) {
                var numChars = 0
                while (lineCount < ts.lineMax && numChars < ts.content.length) {
                    val value = ts.paint.breakText(
                        content.substring(numChars),
                        true,
                        ts.screenWidth.toFloat(),
                        null
                    )
                    numChars += value
                    lineCount++
                }

                // Retrieve the String to be displayed in the current textview
                pageContent += content.substring(0, numChars)
                content = content.substring(numChars)

                val start = symbolsCount
                symbolsCount += pageContent.length
                val end = symbolsCount

                addpage(pages, start, end, ts.content)

                lineCount = 0
                pageContent = ""
            }

        if (lineCount != 0) {
            val start = symbolsCount
            symbolsCount += pageContent.length
            val end = symbolsCount + pageContent.length

            addpage(pages, start, end, ts.content)
        }
        finish(pages)
    }

    private fun addpage(pages: Pages, start: Int, end: Int,
                        content: String) {
        pages.pageCount++
        pages.pageStartEnd.add(Pair(start, end))

        val part  = content.substring(start, end)
        val words = part.split(" ").size
        val symbols = part.length
        Log.d("addpage!", ""+start+" "+end+" "+content.length)

        pages.pageWordsSymbols.add(Pair(words, symbols))
    }

}