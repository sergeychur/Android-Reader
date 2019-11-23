package ru.tp_project.androidreader.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.User
import ru.tp_project.androidreader.view_models.CommonStatsViewModel

class CommonStatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_common_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observer = Observer<User> { statistic: User ->
            run {
                setIntToTextView(view, R.id.books_read_val, statistic.booksRead)
                setIntToTextView(view, R.id.pages_read_val, statistic.pagesRead)
                setIntToTextView(view, R.id.words_read_val, statistic.wordsRead)
                setIntToTextView(view, R.id.hours_per_day_val, statistic.hoursPerDay)
                setIntToTextView(view, R.id.pages_per_hour_val, statistic.pagesPerHour)
                setIntToTextView(view, R.id.words_per_min_val, statistic.wordsPerMin)
                setIntToTextView(view, R.id.years_spent_val, statistic.years)
                setIntToTextView(view, R.id.days_spent_val, statistic.days)
                setIntToTextView(view, R.id.hours_spent_val, statistic.hours)
            }
        }

        ViewModelProviders.of(activity!!).get(CommonStatsViewModel::class.java).getStatistic()
            .observe(this, observer)
    }

    private fun setIntToTextView(view: View, id: Int, value: Int) {
        view.findViewById<TextView>(id).text = value.toString()
    }
}
