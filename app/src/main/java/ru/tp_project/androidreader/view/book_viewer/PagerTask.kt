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

class PagerTask(
    private val mPager: ViewPager?,
    private val currPage: Int,
    val f: (progress: BookViewer.ProgressTracker) -> Unit
) :
    AsyncTask<BookViewer.ViewAndPaint, BookViewer.ProgressTracker, Void>() {

    override fun doInBackground(vararg vps: BookViewer.ViewAndPaint): Void? {

        val vp = vps[0]
        val progress = BookViewer.ProgressTracker()
        val paint = vp.paint
        var lineCount = 0
        val maxLineCount = vp.maxLineCount
        var totalCharactersProcessedSoFar = 0

        // contentString is the whole string of the book
        var totalPages = 0
        Log.d("write! ", "bigstart")
        val parts = vp.contentString.split("\n")
        var stringToBeDisplayed = ""
        var first = true
        addpage(progress, totalPages, 0, 0)
        totalPages++
        for (onePart in parts) {
            var part = onePart
            if (!first) {
                part = "\n" + part
                lineCount++
            }
            first = false
            Log.d("part", part)
            while (part.isNotEmpty()) {
                var numChars = 0
                while (lineCount < maxLineCount && numChars < part.length) {
                    val value = paint.breakText(
                        part.substring(numChars),
                        true,
                        vp.screenWidth.toFloat(),
                        null
                    )
                    numChars += value
                    lineCount++
                }

                // Retrieve the String to be displayed in the current textview
                stringToBeDisplayed += part.substring(0, numChars)
                part = part.substring(numChars)

                if (lineCount < maxLineCount) {
                    continue
                }

                val start = totalCharactersProcessedSoFar
                totalCharactersProcessedSoFar += stringToBeDisplayed.length
                val end = totalCharactersProcessedSoFar
                addpage(progress, totalPages, start, end)

                Log.d("expected! ", stringToBeDisplayed)

                // reset per page items
                lineCount = 0
                stringToBeDisplayed = ""

                // increment  page counter
                totalPages++
            }
        }
        if (lineCount != 0) {
            val start = totalCharactersProcessedSoFar
            totalCharactersProcessedSoFar += stringToBeDisplayed.length
            val end = totalCharactersProcessedSoFar
            addpage(progress, totalPages, start, end)
            totalPages += 1
        }

        return null
    }

    private fun addpage(
        progress: BookViewer.ProgressTracker, totalPages: Int,
        start: Int, end: Int
    ) {
        // publish progress
        progress.addPage(start, end)
        progress.totalPages = totalPages
        publishProgress(progress)
    }

    override fun onProgressUpdate(vararg values: BookViewer.ProgressTracker) {
        Log.d("write! ", "update ")
        f(values[0])
        mPager!!.currentItem = currPage
    }
}


class PagesDraw(val f: () -> Unit):
    AsyncTask<Pages, BookViewer.ProgressTracker, Void>() {

    override fun doInBackground(vararg pages: Pages?): Void? {
        draw(pages[0]!!)
        return null
    }

    fun draw(pages : Pages) {
        Log.d("draw(pages)", "draw(pages)")
        val progress = BookViewer.ProgressTracker()
        for (page in pages.pageStartEnd) {
            Log.d("draw(pages)", "page "+page.first+" "+page.second)
            addpage(progress, page.first, page.second)
        }
        f()
    }

    private fun addpage(progress: BookViewer.ProgressTracker, start: Int, end: Int) {
        progress.addPage(start, end)
        publishProgress(progress)
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