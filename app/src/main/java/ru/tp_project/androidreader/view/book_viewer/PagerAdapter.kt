package ru.tp_project.androidreader.view.book_viewer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class MyPagerAdapter(fragmentManager: FragmentManager,
                     totalPages: Int, var mPhoto: String) :
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
        return PageContentsFragmentBase.create(position, mPhoto)
    }

    fun incrementPageCount() {
        count += 1
        notifyDataSetChanged()
    }
}
