package ru.tp_project.androidreader.view.tasks_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.databinding.FragmentTasksListBinding

class TasksListFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentTasksListBinding
    private lateinit var adapter: TasksListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_tasks_list, container, false)
        viewDataBinding = FragmentTasksListBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProviders.of(this@TasksListFragment).get(TasksListViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.viewmodel?.fetchTasksList(requireContext())

        setupAdapter()
        setupObservers()
    }

    private fun setupObservers() {
        viewDataBinding.viewmodel?.tasksListLive?.observe(viewLifecycleOwner, Observer {
            adapter.updateTasksList(it)
        })

//        viewDataBinding.viewmodel?.toastMessage?.observe(viewLifecycleOwner, Observer {
//            activity?.longToast(it)
//        })

    }

    private fun setupAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            adapter = TasksListAdapter(viewDataBinding.viewmodel!!)
            val layoutManager = LinearLayoutManager(activity)
            tasks_list_rv.layoutManager = layoutManager
            tasks_list_rv.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))
            tasks_list_rv.adapter = adapter
        }
    }

}
