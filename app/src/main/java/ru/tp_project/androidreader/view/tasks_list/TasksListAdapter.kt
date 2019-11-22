package ru.tp_project.androidreader.view.tasks_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.tp_project.androidreader.databinding.ViewTasksListTaskBinding
import ru.tp_project.androidreader.model.data_models.Task

class TasksListAdapter(private val tasksListViewModel: TasksListViewModel) : RecyclerView.Adapter<TasksListViewHolder>() {
    var tasksList: List<Task> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ViewTasksListTaskBinding.inflate(inflater, parent, false)
        return TasksListViewHolder(dataBinding, tasksListViewModel)
    }

    override fun getItemCount() = tasksList.size

    override fun onBindViewHolder(holder: TasksListViewHolder, position: Int) {
        holder.setup(tasksList[position])
    }

    fun updateTasksList(tasksList: List<Task>) {
        this.tasksList = tasksList
        notifyDataSetChanged()
    }
}