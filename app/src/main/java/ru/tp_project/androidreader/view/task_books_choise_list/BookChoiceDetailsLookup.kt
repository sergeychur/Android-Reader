package ru.tp_project.androidreader.view.task_books_choise_list

import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class BookChoiceDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null && event.action == ACTION_DOWN) {
            return (recyclerView.getChildViewHolder(view) as BooksChoiceViewHolder)
                .getItemDetails()
        }
        return null
    }
}