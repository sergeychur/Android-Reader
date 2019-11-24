package ru.tp_project.androidreader.view.tasks_list

import android.view.View
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


import ru.tp_project.androidreader.BR
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.Task
import ru.tp_project.androidreader.model.data_models.TaskStat

class TasksListViewHolder constructor(private val dataBinding: ViewDataBinding, private val tasksListViewModel: TasksListViewModel)
    : RecyclerView.ViewHolder(dataBinding.root) {

//    val avatarImage = itemView.item_avatar
    fun setup(itemData: TaskStat) {
        dataBinding.setVariable(BR.data, itemData)
        dataBinding.executePendingBindings()

//        Picasso.get().load(itemData.owner.avatar_url).into(avatarImage)
        // TODO, change bundle content to smth, that is ok
        itemView.setOnClickListener { v ->
            val bundle = bundleOf("url" to "kek")
            findNavController(v).navigate(R.id.action_tasksListFragment_to_taskEditingFragment, bundle)
        }
}
}