package ru.tp_project.androidreader.view.book_viewer

import android.os.Bundle
import android.text.TextPaint
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import ru.tp_project.androidreader.R

import java.util.HashMap

class BookViewer : AppCompatActivity() {
    private var mPager: ViewPager? = null
    private var mPagerAdapter: FragmentPagerAdapter? = null
    private var mPages: Map<String, String> = HashMap()
    private var mPageIndicator: LinearLayout? = null
    private var mProgressBar: ProgressBar? = null
    private var mContentString = ""
    private var mPagesAmount = 0
    private var mPagesCurrent = 0
    private var mPhoto = ""
    private var mDisplay: Display? = null

    private val screenWidth: Int
        get() {
            val horizontalMargin =
                getResources().getDimension(R.dimen.activity_horizontal_margin) * 2
            return (mDisplay!!.width - horizontalMargin).toInt()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.book_viewer_main)
        mProgressBar = findViewById(R.id.progress) as ProgressBar

        val textviewPage = getLayoutInflater().inflate(
            R.layout.book_viewer_fragment,
            getWindow().getDecorView().findViewById(android.R.id.content) as ViewGroup,
            false
        ) as ViewGroup
        val layout = textviewPage.findViewById(R.id.mText) as LinearLayout
        val contentTextView = layout.findViewById(R.id.text) as TextView

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.pager) as ViewPager

        getFromIntent()

        // obtaining screen dimensions
        mDisplay = getWindowManager().getDefaultDisplay()

        val vp = ViewAndPaint(
            contentTextView.paint,
            textviewPage,
            screenWidth,
            getMaxLineCount(contentTextView),
            mContentString
        )

        val pt = PagerTask(mPager, mPagesCurrent, {v -> this.onPageProcessedUpdate(v)})
        pt.execute(vp)
    }

    private fun getMaxLineCount(view: TextView): Int {

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        val verticalMargin = getResources().getDimension(R.dimen.activity_vertical_margin)
        val paint = view.paint

        //Working Out How Many Lines Can Be Entered In The Screen
        val fm = paint.fontMetrics
        var textHeight = fm.top - fm.bottom
        textHeight = Math.abs(textHeight)

        var maxLineCount = ((height - verticalMargin) / textHeight).toInt()

        return maxLineCount+2
    }

    fun getFromIntent() {
        var strs = intent.getSerializableExtra("book") as ArrayList<String>

        mPagesCurrent = intent.getSerializableExtra("pages_current") as Int
        mPhoto = intent.getStringExtra("image")!!

        mContentString = ""
        for (str in strs) {
            mContentString += str
        }
    }

    private fun initViewPager() {
        mPagerAdapter = MyPagerAdapter(getSupportFragmentManager(), 1, mPhoto)
        mPager!!.setAdapter(mPagerAdapter)
        mPager!!.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                Log.d("notag", ""+position)
                showPageIndicator(position)
            }
        })
    }

    fun onPageProcessedUpdate(progress: ProgressTracker) {
        mPages = progress.pages
        // init the pager if necessary
        if (mPagerAdapter == null) {
            initViewPager()
            hideProgress()
            addPageIndicator(0)
        } else {
            (mPagerAdapter as MyPagerAdapter).incrementPageCount()
            addPageIndicator(progress.totalPages)
            mPagesAmount =  progress.totalPages+1
        }
    }

    private fun hideProgress() {
        mProgressBar!!.visibility = View.GONE
    }

    private fun addPageIndicator(pageNumber: Int) {
        mPageIndicator = findViewById(R.id.pageIndicator) as LinearLayout
        val view = View(this)
        val params = TableLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            1f
        )
        view.layoutParams = params
        Log.d("view", "number "+pageNumber)
        if (pageNumber == 0) {
            view.setBackgroundResource(R.drawable.current_page_indicator)
        } else {
            view.setBackgroundResource(R.drawable.indicator_background)
        }
        view.tag = pageNumber
        mPageIndicator!!.addView(view)

        var textview = findViewById(R.id.pages) as TextView
        textview.setText((mPagesCurrent+1).toString()+" из "+mPagesAmount.toString())


    }

    protected fun showPageIndicator(position: Int) {
        var textview = findViewById(R.id.pages) as TextView
        textview.setText((position+1).toString()+" из "+mPagesAmount.toString())

        try {
            mPageIndicator = findViewById(R.id.pageIndicator) as LinearLayout
            val selectedIndexIndicator = mPageIndicator!!.getChildAt(position)
            selectedIndexIndicator.setBackgroundResource(R.drawable.current_page_indicator)
            // dicolorize the neighbours
            if (position > 0) {
                val leftView = mPageIndicator!!.getChildAt(position - 1)
                leftView.setBackgroundResource(R.drawable.indicator_background)
            }
            if (position < mPages.size) {
                val rightView = mPageIndicator!!.getChildAt(position + 1)
                rightView.setBackgroundResource(R.drawable.indicator_background)
            }


        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

    }

    public fun getContents(pageNumber: Int): String {
        val page = pageNumber.toString()
        val textBoundaries = mPages[page]
        if (textBoundaries != null) {
            val bounds =
                textBoundaries.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val startIndex = Integer.valueOf(bounds[0])
            val endIndex = Integer.valueOf(bounds[1])
            return mContentString.substring(startIndex, endIndex).trim { it <= ' ' }
        }
        return ""
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    class ViewAndPaint(
        var paint: TextPaint,
        var textviewPage: ViewGroup,
        var screenWidth: Int,
        var maxLineCount: Int,
        var contentString: String
    )

    class ProgressTracker {

        var totalPages: Int = 0
        var pages: MutableMap<String, String> = HashMap()

        fun addPage(page: Int, startIndex: Int, endIndex: Int) {
            val thePage = page.toString()
            val indexMarker = "$startIndex,$endIndex"
            pages[thePage] = indexMarker
        }
    }

    companion object {

        private val TAG = "BookView"
    }
}
