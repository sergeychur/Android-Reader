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

class TasksListViewHolder constructor(private val dataBinding: ViewDataBinding, private val tasksListViewModel: TasksListViewModel)
    : RecyclerView.ViewHolder(dataBinding.root) {

    fun setup(itemData: TaskStat, deleteListener: (Int, () -> Unit) -> Unit, shareListener: (Int) -> Unit) {
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
            shareListener(itemData.id)
        }

        val editor = itemView.findViewById<ImageButton>(R.id.edit_task)
        editor.setOnClickListener {v ->
            Log.d("kek", "Move to edit task")
            val bundle = bundleOf("taskId" to itemData.id)
            findNavController(v).navigate(R.id.action_tasksListFragment_to_taskEditingFragment, bundle)
        }

        val stater = itemView.findViewById<ImageButton>(R.id.task_stats)
        stater.setOnClickListener {v->
            Log.d("kek", "move to statistics of task")
            val bundle = bundleOf("taskId" to itemData.id)
            findNavController(v).navigate(R.id.action_tasksListFragment_to_taskEditingFragment, bundle)
        }
    }
}