package ru.tp_project.androidreader.view.book_viewer

import android.os.Bundle
import androidx.fragment.app.Fragment

open class PageContentsFragmentBase : Fragment() {
    /**
     * Returns the page number represented by this fragment object.
     */
    var pageNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = getArguments()!!.getInt(ARG_PAGE)
    }

    companion object {

        val ARG_PAGE = "page"

        fun create(pageNumber: Int, mPhoto:String): PageContentsFragmentBase {
            var fragment: PageContentsFragmentBase? = null

            fragment = PageContentsFragment(mPhoto)

            val args = Bundle()
            args.putInt(ARG_PAGE, pageNumber)
            fragment!!.setArguments(args)
            return fragment
        }
    }

}
