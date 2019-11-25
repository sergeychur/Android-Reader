package ru.tp_project.androidreader.view.tasks_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.databinding.ViewTasksListTaskBinding
import ru.tp_project.androidreader.model.data_models.Task
import ru.tp_project.androidreader.model.data_models.TaskStat

class TasksListAdapter(private val tasksListViewModel: TasksListViewModel,
                       private val deleteListener: (Int, () -> Unit) -> Unit,
                       private val shareListener: (Int) -> Unit)
    : RecyclerView.Adapter<TasksListViewHolder>() {
    var tasksList: List<TaskStat> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ViewTasksListTaskBinding.inflate(inflater, parent, false)
        return TasksListViewHolder(dataBinding, tasksListViewModel)
    }

    override fun getItemCount() = tasksList.size

    override fun onBindViewHolder(holder: TasksListViewHolder, position: Int) {
        holder.setup(tasksList[position], deleteListener, shareListener)
    }

    fun updateTasksList(tasksList: List<TaskStat>) {
        this.tasksList = tasksList
        notifyDataSetChanged()
    }
}