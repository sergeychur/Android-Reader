package ru.tp_project.androidreader.view.book_viewer

import android.os.Bundle
import androidx.fragment.app.Fragment

open class PageContentsFragmentBase : Fragment() {
    /**
     * Returns the page number represented by this fragment object.
     */
    var pageNumber: Int = 0
    var bookAuthor: String = ""//getString(R.string.no_author)
    var bookTitle: String = ""//getString(R.string.no_title)
    var bookGenre : String = ""//getString(R.string.not_set)
    var bookPhoto : String = ""//getString(R.string.not_set)
    var bookSource : String = ""//getString(R.string.not_set)
    var bookDate : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = arguments!!.getInt(ARG_PAGE)
        bookGenre = arguments!!.getString(ARG_GENRE)!!
        bookTitle = arguments!!.getString(ARG_TITLE)!!
        bookPhoto = arguments!!.getString(ARG_PHOTO)!!
        bookSource = arguments!!.getString(ARG_SOURCE)!!
        bookAuthor  = arguments!!.getString(ARG_AUTHOR)!!
        bookDate  = arguments!!.getString(ARG_DATE)!!
    }

    companion object {

        const val ARG_PAGE = "page"
        const val ARG_TITLE = "title"
        const val ARG_AUTHOR = "author"
        const val ARG_GENRE = "genre"
        const val ARG_PHOTO = "photo"
        const val ARG_SOURCE = "source"
        const val ARG_DATE = "date"

        fun create(pageNumber: Int, author:String, genre:String,
                   photo:String, source:String, title:String,
                   date:String): PageContentsFragmentBase {
            val fragment: PageContentsFragmentBase?

            fragment = PageContentsFragment()

            val args = Bundle()
            args.putInt(ARG_PAGE, pageNumber)
            args.putString(ARG_AUTHOR, author)
            args.putString(ARG_GENRE, genre)
            args.putString(ARG_PHOTO, photo)
            args.putString(ARG_SOURCE, source)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_DATE, date)
            fragment.arguments = args
            return fragment
        }
    }

}
