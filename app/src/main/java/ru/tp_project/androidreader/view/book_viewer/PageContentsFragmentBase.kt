package ru.tp_project.androidreader.view.book_viewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.book_viewer_text_view.view.*
import ru.tp_project.androidreader.R

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
        pageNumber = getArguments()!!.getInt(ARG_PAGE)
        bookGenre = getArguments()!!.getString(ARG_GENRE)!!
        bookTitle = getArguments()!!.getString(ARG_TITLE)!!
        bookPhoto = getArguments()!!.getString(ARG_PHOTO)!!
        bookSource = getArguments()!!.getString(ARG_SOURCE)!!
        bookAuthor  = getArguments()!!.getString(ARG_AUTHOR)!!
        bookDate  = getArguments()!!.getString(ARG_DATE)!!
    }

    companion object {

        val ARG_PAGE = "page"
        val ARG_TITLE = "title"
        val ARG_AUTHOR = "author"
        val ARG_AUTHOR_FIRST = "author_first"
        val ARG_AUTHOR_LAST = "author_last"
        val ARG_GENRE = "genre"
        val ARG_PHOTO = "photo"
        val ARG_SOURCE = "source"
        val ARG_DATE = "date"

        fun create(pageNumber: Int, author:String, genre:String,
                   photo:String, source:String, title:String,
                   date:String): PageContentsFragmentBase {
            var fragment: PageContentsFragmentBase? = null

            fragment = PageContentsFragment()

            val args = Bundle()
            args.putInt(ARG_PAGE, pageNumber)
            args.putString(ARG_AUTHOR, author)
            args.putString(ARG_GENRE, genre)
            args.putString(ARG_PHOTO, photo)
            args.putString(ARG_SOURCE, source)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_DATE, date)
            fragment!!.setArguments(args)
            return fragment
        }
    }

}
