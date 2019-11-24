package ru.tp_project.androidreader.view.tasks_list

import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


import ru.tp_project.androidreader.BR
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.TaskStat

class TasksListViewHolder constructor(private val dataBinding: ViewDataBinding, private val tasksListViewModel: TasksListViewModel)
    : RecyclerView.ViewHolder(dataBinding.root) {

    fun setup(itemData: TaskStat) {
        dataBinding.setVariable(BR.data, itemData)
        dataBinding.executePendingBindings()

        // TODO, change bundle content to smth, that is ok
        itemView.setOnClickListener { v ->
            val bundle = bundleOf("url" to "kek")
            findNavController(v).navigate(R.id.action_tasksListFragment_to_taskEditingFragment, bundle)
        }
}
}