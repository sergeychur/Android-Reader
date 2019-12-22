package ru.tp_project.androidreader.view.book_viewer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.tp_project.androidreader.model.xml.BookXML


class MyPagerAdapter(fragmentManager: FragmentManager,
                     totalPages: Int, var bookXML: BookXML) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return count
    }

    private var count: Int = 10
        set

    init {
        this.count = totalPages
    }

    override fun getItem(position: Int): Fragment {
        return PageContentsFragmentBase.create(position,
            bookXML.description.titleInfo.author.first_name+
                    bookXML.description.titleInfo.author.last_name,
            bookXML.description.titleInfo.genre, bookXML.binary,
            bookXML.description.publishInfo.publisher,
            bookXML.description.titleInfo.book_title,
            bookXML.description.titleInfo.date)
    }

    fun incrementPageCount() {
        count += 1
        notifyDataSetChanged()
    }
}
