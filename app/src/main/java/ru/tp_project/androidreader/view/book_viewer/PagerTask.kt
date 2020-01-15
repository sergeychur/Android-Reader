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
        var pages = Pages(0,ts.bookID, ArrayList(), ArrayList(),
            ArrayList(),ts.screenWidth, ts.lineMax,0, 0)

        var lineCount = 0
        addpage(pages, 0,0,0, 0, "") // добавляем обложку
        var pageContent = ""

        var pageStart = 0
        var pageEnd = 0

        var symbolStart = 0
        var symbolEnd = 0

        for (i in ts.content.indices) {
            var content = ts.content[i]
            var offset = 0
            while (content.isNotEmpty()) {
                var numChars = 0
                while (lineCount < ts.lineMax && numChars < content.length) {
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
                offset += numChars

                if (lineCount < ts.lineMax) {
                    continue
                }

                pageEnd = i
                symbolEnd = offset
                addpage(pages, pageStart, symbolStart, pageEnd, symbolEnd, pageContent)
                pageStart = pageEnd
                symbolStart = symbolEnd

                Log.d("done", ""+pageContent)
                lineCount = 0
                pageContent = ""
            }
        }
        Log.d("tick11", "no bug here")
        if (lineCount != 0) {
            addpage(pages, pageStart, symbolStart,
                ts.content.size-1, ts.content[ts.content.size-1].length, pageContent)
        }
        Log.d("tick12", "no bug here")
        finish(pages)
        Log.d("tick13", "no bug here")
    }

    private fun addpage(pages: Pages,
                        startPage: Int, startSymbol: Int,
                        endPage: Int, endSymbol: Int,
                        pageContent: String) {
        pages.pageCount++
        pages.pageStart.add(Pair(startPage, startSymbol))
        pages.pageEnd.add(Pair(endPage, endSymbol))

        val words = pageContent.split(" ").size
        val symbols = pageContent.length

        pages.pageWordsSymbols.add(Pair(words, symbols))
    }

}