package ru.tp_project.androidreader.fragments

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.models.Book
import ru.tp_project.androidreader.viewModels.BookShelveViewModel
import java.util.ArrayList
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.shadow.ShadowViewDelegate
import ru.tp_project.androidreader.data.database.BookDao
import ru.tp_project.androidreader.repository.BookRepository
import ru.tp_project.androidreader.databinding.ShelveOneBookBinding

class BookShelfFragment : Fragment() {

    var books = ArrayList<Book>()
/*
    private val viewModel: BookShelveViewModel by viewModels(
        // SavedStateVMFactory is renamed to SavedStateViewModelFactory
        // https://stackoverflow.com/questions/56908490/unresolved-reference-savedstatevmfactory

        // first argument is application
        //https://stackoverflow.com/questions/57838759/how-android-jetpack-savedstateviewmodelfactory-works

        // context.
        //https://stackoverflow.com/questions/57468124/what-type-of-application-does-savedstateviewmodelfactory-requires

        factoryProducer = {
            //first argument can be context.applicationContext!! as Application
            SavedStateViewModelFactory(this.activity!!.application, this)
        }
    )*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_shelve, container, false)
        val recyclerView = view.findViewById(R.id.listRecyclerView) as RecyclerView

        recyclerView.adapter = ListAdapter(books, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        Log.d("adapters", "в ините "+ books.size + " " + recyclerView.id )
        return view
    }

    fun updateTODO() {

    }

    val bookObserver = Observer<Book> { newBook ->
        // Update the UI, in this case, a TextView.
        //nameTextView.text = newName
        updateTODO()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            /*
        //val rep = BookRepository()
        //val state = SavedStateHandle()
        //state.set("bookId", "1")
        //+val voewM =  SavedStateViewModelFactory(this.activity!!.application, this)
        //val viewModel: BookShelveViewModel = BookShelveViewModel(SavedStateHandle(), rep)
        val viewModel = SavedStateViewModelFactory(this.activity!!.application, this)
        val observer = Observer<Book> { statistic: Book ->
            run {
               //
            }
        }

        //ViewModelProviders.of(activity!!).get(BookShelveViewModel::class.java).book
        //    .observe(this, observer)

        //viewModel.book.observe(this, observer)
        //viewModel.book.observe(viewLifecycleOwner, bookObserver)*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val a = Book("1", "Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст")

        val b = Book("2", "Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст")
        val c = Book("3", "Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст")
        val d = Book("4", "Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст")
        val e = Book("4","Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст")
        val f = Book("5","Война и мир", "no",
            "Лев Николаевич Толстой", 32.3f, "FB2", 0.3f,
            "Это какой то текст")
        books = ArrayList<Book>()
        books.add(a)
        books.add(b)
        books.add(c)
        books.add(d)
        books.add(e)
        books.add(f)
        Log.d("createAll", "в ините "+ books.size)
        //list = ListFragment(books)
    }

}

class ListAdapter(private val books: List<Book>, val fragment: Fragment) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindView(position, fragment)

        val vm = ViewModelProviders.of((FragmentActivity) holder.getContext()).get(BookShelveViewModel.class);

        vm.setPost(list.get(position));
        holder.setViewModel(vm);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ListViewHolder {
        Log.d("yuyuyu", "в ините "+ books.size + " " + parent.id + " " +
                R.layout.fragment_book_shelve + " " + R.id.listRecyclerView)

        // val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_book_shelve, parent, false)

        val view = LayoutInflater.from(parent.context).inflate(R.layout.shelve_one_book,
            parent, false)

        var binding : ShelveOneBookBinding
        binding = inflate(LayoutInflater.from(parent.getContext()), R.layout.shelve_one_book,
            parent, false)
        return ListViewHolder(view, books, fragment, binding, parent.getContext() )
    }

    override fun getItemCount(): Int {
        return books.size
    }

    class ListViewHolder(itemView: View, private val books: List<Book>,
                         val fragment: Fragment,
                         val binding : ShelveOneBookBinding,
                         val context: Context
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        //private List<TextView> views = new ArrayList<>(4);
        private val text: TextView? = null
        private var viewsInRow: Int = 0
        var viewModel: BookShelveViewModel
        get() = viewModel
        set(value) {
            this.viewModel = value;
            binding.book = value
        }

       // private lateinit var binding: ShelveOneBookBinding

        init {
            Log.d("here we go", "в ините "+ books.size)
            Log.d("myTag", "в ините "+ books.size)
            Log.d("itemView", "в ините "+ books.size + " " + itemView.id + " " + R.layout.fragment_book_shelve + " " + R.id.listRecyclerView)
            itemView.setOnClickListener(this)
//            binding = ShelveOneBookBinding.bind(itemView).apply {
//                book = ViewModelProviders.of(this@BookShelfFragment).get(TasksListViewModel::class.java)
//                setLifecycleOwner(viewLifecycleOwner)
//            }
//            binding.book = BookModel
//            binding.book?.data.value = books[3]

            viewsInRow = 3
            if (itemView.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.d("myTag", "Портретная ориентация")
            } else if (itemView.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.d("myTag", "Альбомная ориентация")
                viewsInRow = 4
            }
        }

        override fun onClick(v: View) {

        }

        fun bindView(num: Int, fragment1 : Fragment) {
            //val binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
            //binding.setEmployee(employee);

            if (binding.book == null) {
                Log.d("iiiiiii", "binding.book == null")
                //return
            }
            if (binding.book?.data == null) {
                Log.d("iiiiiii", "binding.book.data == null")
                //return
            }
            binding.book?.refresh()
            val observer = Observer<Book> { statistic: Book? ->            }
            binding.book?.refresh()
            binding.book?.data!!.observe(fragment1.viewLifecycleOwner, observer)


            Log.d("not there", "в ините "+ books.size)
            val book = books[num]
            Log.d("myTag", num.toString() + "This is my message " + books[num])

            val bookNameView = itemView.findViewById<View>(R.id.bookName) as TextView
            bookNameView.setText(book.name)

            val bookAutorView = itemView.findViewById<View>(R.id.bookAuthor) as TextView
            bookAutorView.setText(book.author)

            val bookFormatView = itemView.findViewById<View>(R.id.bookFormat) as TextView
            bookFormatView.setText(book.format)

            val bookPreviewView = itemView.findViewById<View>(R.id.bookPreview) as ImageView
            val imageBytes = Base64.decode(book.photo, 0)
            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            bookPreviewView.setImageBitmap(image)

            val bookSizeView = itemView.findViewById<View>(R.id.bookSize) as TextView
            bookSizeView.setText(book.size.toString())

            val bookProgressView = itemView.findViewById<View>(R.id.bookProgress) as SeekBar
            bookProgressView.setProgress(book.progress.toInt())
        }
    }
}
