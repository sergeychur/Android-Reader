package ru.tp_project.androidreader.fragments

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shadow.ShadowViewDelegate
import com.squareup.picasso.Picasso
import ru.tp_project.androidreader.data.database.BookDao
import ru.tp_project.androidreader.repository.BookRepository
import ru.tp_project.androidreader.databinding.ShelveOneBookBinding
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import javax.xml.parsers.DocumentBuilderFactory

class BookShelfFragment : Fragment() {
    var books = ArrayList<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_shelve, container, false)
        val recyclerView = view.findViewById(R.id.listRecyclerView) as RecyclerView

        var addButton = view.findViewById(R.id.addBook) as FloatingActionButton;
        addButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                showFileChooser()
            }
        })

        recyclerView.adapter = ListAdapter(books, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun showFileChooser() {

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
            val path = selectedFile?.lastPathSegment.toString().removePrefix("raw:")
            println(selectedFile?.lastPathSegment)

            var contentResolver ContentResolver
            val input: InputStream = ContentResolver.openInputStream(data!!.data)
            val inputAsString = input.bufferedReader().use { it.readText() }
            textView.setText(inputAsString)

            //println("fly like apple "+path + " " + getTextContent(path))
            //dosomefun(getUriRealPath(this.context!!, data?.data!!))
                //val xlmFile: File = File(data)
        }
    }

    fun getTextContent(pathFilename: String): String {

        val fileobj = File( pathFilename )

        if (!fileobj.exists()) {

            println("Path does not exist")

        } else {

            println("Path to read exist")
        }

        println("Path to the file:")
        println(pathFilename)

        if (fileobj.exists() && fileobj.canRead()) {

            var ins: InputStream = fileobj.inputStream()
            var content = ins.readBytes().toString(Charset.defaultCharset())
            return content

        }else{

            return "Some error, Not found the File, or app has not permissions: " + pathFilename
        }
    }
    fun dosomefun(name: String?) {


//        var intent = Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("file/*");
//        startActivityForResult(intent, YOUR_RESULT_CODE);
        Log.d("lololo name", name)
        val xlmFile: File = File(name)
        val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xlmFile)

        xmlDoc.documentElement.normalize()

        println("Root Node:" + xmlDoc.documentElement.nodeName)

        val bookList: NodeList = xmlDoc.getElementsByTagName("book")

        for(i in 0..bookList.length - 1)
        {
            var bookNode: Node = bookList.item(i)

            if (bookNode.getNodeType() === Node.ELEMENT_NODE) {

                val elem = bookNode as Element


                val mMap = mutableMapOf<String, String>()


                for(j in 0..elem.attributes.length - 1)
                {
                    mMap.put(elem.attributes.item(j).nodeName, elem.attributes.item(j).nodeValue)
                }
                println("Current Book : ${bookNode.nodeName} - $mMap")

                println("Author: ${elem.getElementsByTagName("author").item(0).textContent}")
                println("Title: ${elem.getElementsByTagName("title").item(0).textContent}")
                println("Genre: ${elem.getElementsByTagName("genre").item(0).textContent}")
                println("Price: ${elem.getElementsByTagName("price").item(0).textContent}")
                println("publish_date: ${elem.getElementsByTagName("publish_date").item(0).textContent}")
                println("description: ${elem.getElementsByTagName("description").item(0).textContent}")
            }
        }
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
        var viewModel = BookShelveViewModel(fragment.context!!)
        viewModel.refresh()
        val observer = Observer<Book> { newBook: Book? ->
            holder.binding.book = newBook
            Log.d("interesting", "binding book"+ " "+newBook)
        }
        viewModel.data?.observe(fragment.viewLifecycleOwner, observer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ListViewHolder {
        val inflatter = LayoutInflater.from(parent.getContext())
        //var binding : ShelveOneBookBinding
        var binding =  ShelveOneBookBinding.inflate(inflatter,parent, false)

        return ListViewHolder(binding.root, books, fragment, binding, parent.getContext() )
    }

    override fun getItemCount(): Int {
        return books.size
    }

    @BindingAdapter("bind:imageUrl")
    fun loadImage( imageView : ImageView, v:String) {
       Picasso.with(imageView.getContext()).load(v).into(imageView);
    }

    class ListViewHolder(itemView: View, private val books: List<Book>,
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

        fun bindView(num: Int, fragment1 : Fragment) {

//            binding.book?.refresh()
//            val observer = Observer<Book> { statistic: Book? ->            }
//            binding.book?.refresh()
//            binding.book?.data!!.observe(fragment1.viewLifecycleOwner, observer)
            val book = books[num]
            val bookNameView = itemView.findViewById<View>(R.id.bookName) as TextView
            bookNameView.setText(book.name)

            val bookProgressView = itemView.findViewById<View>(R.id.bookProgress) as SeekBar
            bookProgressView.setProgress(book.progress.toInt())
        }
    }
}
