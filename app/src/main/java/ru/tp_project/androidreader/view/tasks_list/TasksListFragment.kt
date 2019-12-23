package ru.tp_project.androidreader.view.tasks_list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.ReaderApp
import ru.tp_project.androidreader.databinding.FragmentTasksListBinding
import ru.tp_project.androidreader.model.data_models.TaskStat

class TasksListFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentTasksListBinding
    private lateinit var adapter: TasksListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentTasksListBinding.inflate(inflater, container, false).apply {
            viewmodel =
                ViewModelProviders.of(this@TasksListFragment).get(TasksListViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isActive = viewDataBinding.viewmodel?.isActive() ?: true
        viewDataBinding.viewmodel?.setTabNum(if (isActive) 0 else 1)
        viewDataBinding.viewmodel?.fetchTasksList(ReaderApp.getInstance(), !isActive)
        val index = if (isActive) 0 else 1
        tabs.getTabAt(index)?.select()
        setupAdapter()
        setupObservers()
        add_tusk_btn.setOnClickListener {
            findNavController().navigate(R.id.newTaskFragment)
        }
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    viewDataBinding.viewmodel?.fetchTasksList(ReaderApp.getInstance(), false)
                    viewDataBinding.viewmodel?.setTabNum(0)
                } else {
                    viewDataBinding.viewmodel?.clearTasksList()
                    viewDataBinding.viewmodel?.fetchTasksList(ReaderApp.getInstance(), true)
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

        // TODO(sergeychur): crutch again, move to PagedList
        viewDataBinding.viewmodel?.changed?.observe(viewLifecycleOwner, Observer {
            adapter.updateTasksList(viewDataBinding.viewmodel?.tasksListLive?.value!!.toList())
        })

    }

    private fun setupAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            adapter =
                TasksListAdapter(viewDataBinding.viewmodel!!, { taskId, callback: () -> Unit ->
                    onDeleteTask(taskId, callback)
                },
                    { task: TaskStat -> onShareTask(task) }
                )
            val layoutManager = LinearLayoutManager(activity)
            tasks_list_rv.layoutManager = layoutManager
            tasks_list_rv.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    layoutManager.orientation
                )
            )
            tasks_list_rv.adapter = adapter
        }
    }

    private fun onDeleteTask(taskId: Int, callback: () -> Unit) {
        viewDataBinding.viewmodel?.deleteTask(ReaderApp.getInstance(), taskId) {
            callback()
        }
    }

    private fun onShareTask(task: TaskStat) {
        Log.d("kek", "share")
        val str = getString(R.string.share_task_text).format(
            task.name, task.description,
            task.books, task.pages, task.words,
            task.booksRead, task.pagesRead, task.wordsRead
        )
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, getString(R.string.share_title))
            putExtra(Intent.EXTRA_TEXT, str)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}
