package ru.tp_project.androidreader.view.book_viewer

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.model.data_models.Pages
import ru.tp_project.androidreader.model.repos.BookRepositoryFS
import ru.tp_project.androidreader.view_models.BookViewerViewModel
import java.io.File
import java.io.InputStream


class BookViewer : AppCompatActivity() {
    private var mPager: ViewPager? = null
    private var mPagerAdapter: FragmentPagerAdapter? = null
    private var mPageIndicator: LinearLayout? = null
    private var mProgressBar: ProgressBar? = null
    private var book: Book? = null
    private var text: List<String>? = null
    private var pages: Pages? = null
    private var viewmodel: BookViewerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.book_viewer_main)
        mProgressBar = findViewById(R.id.progress)

        viewmodel = ViewModelProviders.of(this@BookViewer).get(BookViewerViewModel::class.java)

        mPager = findViewById(R.id.pager)

        val b = getFromIntent()
        book = b
        viewmodel!!.get(applicationContext, book!!.id){ smp ->
            pages = smp
            runOnUiThread {
                initViewPager(smp, b)
                hideProgress()
                for (i in 0..smp.pageCount-1) {
                    setIndicator(i, smp.pageCurrent)
                }
            }
        }
        Log.d("tick16", "no bug")
        val bookLoader = BookRepositoryFS()
        val bookFromFile = bookLoader.getBookFB2FromFile(book!!.path)
        text = bookFromFile!!.body.section
        Log.d("tick17", text!!.joinToString(""))
    }

    private fun getFromIntent(): Book {
        return intent.getSerializableExtra("book") as Book
    }

    private fun initViewPager(pages: Pages, book: Book) {
        mPagerAdapter = MyPagerAdapter(supportFragmentManager, pages.pageCount, book)
        mPager!!.adapter = mPagerAdapter
        mPager!!.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                showPageIndicator(position)
            }
        })
        mPager!!.currentItem = pages.pageCurrent
    }

    private fun hideProgress() {
        mProgressBar!!.visibility = View.GONE
    }

    private fun setIndicator(pageNumber: Int, current: Int) {
        mPageIndicator = findViewById(R.id.pageIndicator)
        val view = View(this)
        val params = TableLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            1f
        )
        view.layoutParams = params
        if (pageNumber == current) {
            setPageText(pageNumber)
            view.setBackgroundResource(R.drawable.current_page_indicator)
        } else {
            view.setBackgroundResource(R.drawable.indicator_background)
        }
        view.tag = pageNumber
        mPageIndicator!!.addView(view)
    }

    private fun setCurrentPage(position: Int) {
        setPageText(position)
        pages!!.pageCurrent = position
        viewmodel!!.update(applicationContext, pages!!)
    }

    private fun setPageText(position: Int) {
        val textview = findViewById<TextView>(R.id.pages)
        val pagesText = (position + 1).toString() + " " +
                getString(R.string.from) + " " +
                pages!!.pageCount.toString()
        textview.text = pagesText

    }

    private fun showPageIndicator(position: Int) {
        setCurrentPage(position)
        try {
            mPageIndicator = findViewById(R.id.pageIndicator)
            val selectedIndexIndicator = mPageIndicator!!.getChildAt(position)
            selectedIndexIndicator.setBackgroundResource(R.drawable.current_page_indicator)
            // dicolorize the neighbours
            if (position > 0) {
                val leftView = mPageIndicator!!.getChildAt(position - 1)
                leftView.setBackgroundResource(R.drawable.indicator_background)
            }
            if (position < pages!!.pageCount) {
                val rightView = mPageIndicator!!.getChildAt(position + 1)
                rightView.setBackgroundResource(R.drawable.indicator_background)
            }
        } catch (e: Exception) {
            Log.e("tag", e.toString())
        }

    }

    fun getContents(pn: Int): String {
        val p = pages
        if (p == null) {
            Log.d("BookViewerError", getString(R.string.noPages))
            return getString(R.string.noPages)
        }

        val b = book
        if (b == null) {
            Log.d("BookViewerError", getString(R.string.noBook))
            return getString(R.string.noBook)
        }

        var pageNumber = pn
        if (pageNumber >= p.pageStart.size) {
            pageNumber = p.pageStart.size-1
        }
        var start = p.pageStart[pageNumber]
        var end = p.pageEnd[pageNumber]

        if (start.first == end.first) {
            return text!![pageNumber].substring(start.second, end.second)
        }

        var row = text!![start.first].substring(start.second)
        for (i in start.first+1..end.first-1 ) {
            row += text!![i]
        }
        row += text!![end.first].substring(0, end.second)

        return row
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
         menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
}
