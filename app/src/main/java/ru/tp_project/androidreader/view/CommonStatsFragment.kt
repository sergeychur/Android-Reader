package ru.tp_project.androidreader.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.tp_project.androidreader.databinding.FragmentCommonStatsBinding

import ru.tp_project.androidreader.model.data_models.User
import ru.tp_project.androidreader.view_models.CommonStatsViewModel

class CommonStatsFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentCommonStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = FragmentCommonStatsBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProviders.of(this@CommonStatsFragment).get(CommonStatsViewModel::class.java)
            setLifecycleOwner(viewLifecycleOwner)
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.viewmodel?.getStatistic()
        val observer = Observer<User> { statistic: User? ->
            
        }
        viewDataBinding.viewmodel?.getStatistic()
        viewDataBinding.viewmodel?.statistic?.observe(viewLifecycleOwner, observer)
    }

}
