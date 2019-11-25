package ru.tp_project.androidreader.view


import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.view_models.BookShelveViewModel
import java.util.ArrayList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.tp_project.androidreader.databinding.ShelveOneBookBinding

class BookShelfFragment : Fragment() {
    var books = ArrayList<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_shelve, container, false)
        val recyclerView = view.findViewById(R.id.listRecyclerView) as RecyclerView

        val addButton = view.findViewById(R.id.addBook) as FloatingActionButton
        addButton.setOnClickListener { showFileChooser() }

        recyclerView.adapter = ListAdapter(books, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return view
    }

    private fun showFileChooser() {

        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            selectedFile.toString()
            Log.d("lololo", selectedFile.toString())
//            val path = selectedFile?.lastPathSegment.toString().removePrefix("raw:")
/*
            var contentResolver ContentResolver
            val input: InputStream = ContentResolver.openInputStream(data!!.data)
            val inputAsString = input.bufferedReader().use { it.readText() }
            textView.setText(inputAsString)
*/
            //println("fly like apple "+path + " " + getTextContent(path))
            //dosomefun(getUriRealPath(this.context!!, data?.data!!))
            //val xlmFile: File = File(data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val a = Book(
            "1", "Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст"
        )

        val b = Book(
            "2", "Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст"
        )
        val c = Book(
            "3", "Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст"
        )
        val d = Book(
            "4", "Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст"
        )
        val e = Book(
            "4", "Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст"
        )
        val f = Book(
            "5", "Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст"
        )
        books = arrayListOf(a, b, c, d, e, f)
        Log.d("createAll", "в ините "+ books.size)
        //list = ListFragment(books)
    }

}

class ListAdapter(private val books: List<Book>, val fragment: Fragment) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val viewModel = BookShelveViewModel(fragment.context!!)
        viewModel.refresh()
        val observer = Observer<Book> { newBook: Book? ->
            holder.binding.book = newBook
            Log.d("interesting", "binding book $newBook")
        }
        viewModel.data.observe(fragment.viewLifecycleOwner, observer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflatter = LayoutInflater.from(parent.context)
        //var binding : ShelveOneBookBinding
        val binding =  ShelveOneBookBinding.inflate(inflatter,parent, false)

        return ListViewHolder(binding.root, books, fragment, binding, parent.context)
    }

    override fun getItemCount(): Int {
        return books.size
    }

//    @BindingAdapter("bind:imageUrl")
//    fun loadImage( imageView : ImageView, v:String) {
//        Picasso.with(imageView.context).load(v).into(imageView);
//    }

    class ListViewHolder(itemView: View, @Suppress("unused") private val books: List<Book>,
                         val fragment: Fragment,
                         val binding : ShelveOneBookBinding,
                         val context: Context
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {

        }

//        fun bindView(num: Int, fragment1 : Fragment) {
//
////            binding.book?.refresh()
////            val observer = Observer<Book> { statistic: Book? ->            }
////            binding.book?.refresh()
////            binding.book?.data!!.observe(fragment1.viewLifecycleOwner, observer)
//            val book = books[num]
//            val bookNameView = itemView.findViewById<View>(R.id.bookName) as TextView
//            bookNameView.setText(book.name)
//
//            val bookProgressView = itemView.findViewById<View>(R.id.bookProgress) as SeekBar
//            bookProgressView.setProgress(book.progress.toInt())
//        }
    }
}
