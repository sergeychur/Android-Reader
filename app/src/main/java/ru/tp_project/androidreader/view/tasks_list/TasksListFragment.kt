package ru.tp_project.androidreader.view.tasks_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import ru.tp_project.androidreader.databinding.FragmentTasksListBinding

class TasksListFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentTasksListBinding
    private lateinit var adapter: TasksListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentTasksListBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProviders.of(this@TasksListFragment).get(TasksListViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isActive = viewDataBinding.viewmodel?.isActive() ?: true
        viewDataBinding.viewmodel?.setTabNum(if (isActive) 0 else 1)
        viewDataBinding.viewmodel?.fetchTasksList(requireContext(), !isActive)
        val index = if (isActive) 0 else 1
        tabs.getTabAt(index)?.select()
        setupAdapter()
        setupObservers()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    viewDataBinding.viewmodel?.fetchTasksList(requireContext(), false)
                    viewDataBinding.viewmodel?.setTabNum(0)
                } else {
                    viewDataBinding.viewmodel?.clearTasksList()
                    viewDataBinding.viewmodel?.fetchTasksList(requireContext(), true)
                    viewDataBinding.viewmodel?.setTabNum(1)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                viewDataBinding.viewmodel?.clearTasksList()
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun setupObservers() {
        viewDataBinding.viewmodel?.tasksListLive?.observe(viewLifecycleOwner, Observer {
            adapter.updateTasksList(it)
        })

    }

    private fun setupAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            adapter = TasksListAdapter(viewDataBinding.viewmodel!!, {taskId, callback:() -> Unit ->
                onDeleteTask(taskId, callback)
            },
                {taskId -> onShareTask(taskId)}
            )
            val layoutManager = LinearLayoutManager(activity)
            tasks_list_rv.layoutManager = layoutManager
            tasks_list_rv.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))
            tasks_list_rv.adapter = adapter
        }
    }

    private fun onDeleteTask(taskId: Int, callback: () -> Unit) {
        viewDataBinding.viewmodel?.deleteTask(requireContext(), taskId) {
            callback()
            viewDataBinding.viewmodel?.clearTasksList()
            val active = viewDataBinding.viewmodel?.isActive()
            val activeVal = active ?: true
            viewDataBinding.viewmodel?.fetchTasksList(requireContext(), !activeVal)
        }
    }

    private fun onShareTask(taskId: Int) {
        Log.d("kek", "share")
    }
}
