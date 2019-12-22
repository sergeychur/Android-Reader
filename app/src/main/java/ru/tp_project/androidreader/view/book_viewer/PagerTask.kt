package ru.tp_project.androidreader.view.book_viewer

import android.content.Context
import android.os.AsyncTask
import android.text.TextPaint
import android.util.Log
import androidx.viewpager.widget.ViewPager
import ru.tp_project.androidreader.R


/**
 * Created by gkoros on 12/03/2017.
 */

class PagerTask(val mPager: ViewPager?, val currPage: Int, val f :  (progress: BookViewer.ProgressTracker) -> Unit) :
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
        var parts = vp.contentString.split("\n")
        var stringToBeDisplayed = ""
        var first = true
        for (onePart in parts) {
            var part = onePart
            if (!first) {
                part = "\n"+part
                lineCount++
            }
            first = false
            Log.d("part", part)
            while (part.length > 0) {
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

                var start = totalCharactersProcessedSoFar
                totalCharactersProcessedSoFar += stringToBeDisplayed.length
                var end = totalCharactersProcessedSoFar
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
            var start = totalCharactersProcessedSoFar
            totalCharactersProcessedSoFar += stringToBeDisplayed.length
            var end = totalCharactersProcessedSoFar
            addpage(progress, totalPages, start, end)
            totalPages++
        }

        return null
    }

    fun addpage(progress : BookViewer.ProgressTracker, totalPages : Int,
                start: Int, end : Int) {
        // publish progress
        progress.totalPages = totalPages
        progress.addPage(totalPages, start, end)
        publishProgress(progress)
    }

    override fun onProgressUpdate(vararg values: BookViewer.ProgressTracker) {
        Log.d("write! ", "update ")
        f(values[0])
        mPager!!.currentItem = currPage
    }

}
