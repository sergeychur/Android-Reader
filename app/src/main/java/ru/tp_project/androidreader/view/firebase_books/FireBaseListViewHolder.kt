package ru.tp_project.androidreader.view.firebase_books

import android.util.Log
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.BR
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.FireBaseBook

class FireBaseListViewHolder(private val dataBinding: ViewDataBinding)
    : RecyclerView.ViewHolder(dataBinding.root) {

    fun setup(itemData: FireBaseBook, deleteListener: (String, () -> Unit) -> Unit,
              downloadListener: (String, String, () -> Unit) -> Unit) {
        dataBinding.setVariable(BR.firebase_book, itemData)
        dataBinding.executePendingBindings()

        val deleter = itemView.findViewById<ImageButton>(R.id.delete_firebase_book)
        deleter.setOnClickListener {
            deleteListener(itemData.link) {
            }
        }

        val downloader = itemView.findViewById<ImageButton>(R.id.download_firebase_book)
        downloader.setOnClickListener {
            downloadListener(itemData.name, itemData.link) {
            }
        }

    }
}