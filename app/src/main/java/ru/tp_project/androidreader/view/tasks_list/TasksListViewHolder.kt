package ru.tp_project.androidreader.view.tasks_list

import android.util.Log
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.BR
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.TaskStat

class TasksListViewHolder(private val dataBinding: ViewDataBinding)
    : RecyclerView.ViewHolder(dataBinding.root) {

    fun setup(itemData: TaskStat, deleteListener: (Int, () -> Unit) -> Unit, shareListener: (TaskStat) -> Unit) {
        dataBinding.setVariable(BR.data, itemData)
        dataBinding.executePendingBindings()

        val deleter = itemView.findViewById<ImageButton>(R.id.delete_task)
        deleter.setOnClickListener {
            deleteListener(itemData.id) {
                Log.println(Log.ERROR, "lol", "kek")
            }
        }

        val sharer = itemView.findViewById<ImageButton>(R.id.share_task)
        sharer.setOnClickListener {
            shareListener(itemData)
        }
    }
}