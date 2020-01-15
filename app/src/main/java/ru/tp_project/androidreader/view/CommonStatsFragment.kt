package ru.tp_project.androidreader.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_common_stats.*
import ru.tp_project.androidreader.R
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
            viewmodel = ViewModelProviders.of(this@CommonStatsFragment)
                .get(CommonStatsViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.viewmodel?.getStatistic()
        val observer = Observer<User> { statistic: User ->
            setIntToTextView(books_read_val, statistic.booksRead)
            setIntToTextView(pages_read_val, statistic.pagesRead)
            setIntToTextView(words_read_val, statistic.wordsRead)
        }
        viewDataBinding.viewmodel?.statistic?.observe(viewLifecycleOwner, observer)
    }

    private fun setIntToTextView(view: TextView, value: Int) {
        view.text = value.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_share).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TITLE, getString(R.string.share_title))
                putExtra(Intent.EXTRA_TEXT, viewDataBinding.viewmodel!!.getStatisticText())
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }
        return super.onOptionsItemSelected(item)
    }
}
