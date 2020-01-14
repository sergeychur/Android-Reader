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
    var content: List<String>,
    var bookID: Int
)

class PagesCount(val finish: (pages: Pages) -> Unit) {
    fun execute(ts: TextSize) {
        doAsync {
            count(ts)
        }.execute()
    }

    fun count(ts: TextSize){

        Log.d("tick10", "no bug here")
        var pages = Pages(0,ts.bookID, ArrayList(),
            ArrayList(),ts.screenWidth, ts.lineMax,0, 0)

        var lineCount = 0
        addpage(pages, 0, 0, "") // добавляем обложку
        var symbolsCount = 0
        var pageContent = ""

        for (page in ts.content) {
            var content = page
            while (content.isNotEmpty()) {
                var numChars = 0
                while (lineCount < ts.lineMax && numChars < page.length) {
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

                addpage(pages, start, end, "")

                lineCount = 0
                pageContent = ""
            }
        }
        Log.d("tick11", "no bug here")
        if (lineCount != 0) {
            val start = symbolsCount
            symbolsCount += pageContent.length
            val end = symbolsCount + pageContent.length

            addpage(pages, start, end, "")
        }
        Log.d("tick12", "no bug here")
        finish(pages)
        Log.d("tick13", "no bug here")
    }

    private fun addpage(pages: Pages, start: Int, end: Int,
                        content: String) {
        pages.pageCount++
        pages.pageStartEnd.add(Pair(start, end))

//        val part  = content.substring(start, end)
        val words = 0//part.split(" ").size
        val symbols = 0//part.length


        pages.pageWordsSymbols.add(Pair(words, symbols))
    }

}