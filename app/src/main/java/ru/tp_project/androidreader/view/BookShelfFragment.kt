package ru.tp_project.androidreader.view


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.view_models.BookShelveViewModel
import java.util.ArrayList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import org.simpleframework.xml.core.Persister
import ru.tp_project.androidreader.databinding.ShelveOneBookBinding
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import ru.tp_project.androidreader.model.xml.BookXML
import java.io.File
import java.io.InputStream
import java.io.StringReader
import java.nio.charset.Charset
import java.util.zip.ZipFile
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

            var filePath: String? = null
            val _uri = data!!.getData()


            val input: InputStream? = getActivity()!!.getContentResolver().openInputStream(data!!.data!!)

            val inputAsString = input!!.bufferedReader().use { it.readText() }
            letstry(inputAsString!!)

            //Log.d("inputAsString ", inputAsString)




            //println("fly like apple "+path + " " + getTextContent(path))
            //dosomefun(getUriRealPath(this.context!!, data?.data!!))
            //val xlmFile: File = File(data)
        }
    }

    fun letstry(xml : String) {
        val reader = StringReader(xml)
        val serializer = Persister()
        Log.v("lolo", "sdadasad")
        try {
            val book = serializer.read(BookXML::class.java, reader, false)
            Log.v("SimpleTest", "Pet Name")
            if (book.stylesheet != null) {
                Log.v("SimpleTest", "success")
                Log.v("SimpleTest", book.toString())
                Log.v("stylesheet type", book.stylesheet.type)
                Log.v("titleInfo book_title", book.description.titleInfo.book_title)
                Log.v("titleInfo date", book.description.titleInfo.date)
                Log.v("titleInfo genre", book.description.titleInfo.genre)
                Log.v("titleInfo lang", book.description.titleInfo.lang)
                Log.v("titleInfo first_name", book.description.titleInfo.author.first_name)
                Log.v("titleInfo last_name", book.description.titleInfo.author.last_name)

                Log.v("SimpleTest", "success")
                Log.v("SimpleTest", "success")
                Log.v("SimpleTest", "success")
                Log.v("SimpleTest", "success")
                Log.v("SimpleTest", "success")
                Log.v("SimpleTest", "success")
            }
        } catch (e: Exception) {
            Log.e("SimpleTest", e.message)
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
    fun dosomefun(inp: String) {

        var inp1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
        "   <s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
                "       <s:Header>"+
                "           <ActivityId CorrelationId=\"15424263-3c01-4709-bec3-740d1ab15a38\" xmlns=\"http://schemas.microsoft.com/2004/09/ServiceModel/Diagnostics\">50d69ff9-8cf3-4c20-afe5-63a9047348ad</ActivityId>"+
                "           <clalLog_CorrelationId xmlns=\"http://clalbit.co.il/clallog\">eb791540-ad6d-48a3-914d-d74f57d88179</clalLog_CorrelationId>"+
                "       </s:Header>"+
                "       <s:Body>"+
                "           <ValidatePwdAndIPResponse xmlns=\"http://tempuri.org/\">"+
                "           <ValidatePwdAndIPResult xmlns:a=\"http://schemas.datacontract.org/2004/07/ClalBit.ClalnetMediator.Contracts\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">"+
                "           <a:ErrorMessage>Valid User</a:ErrorMessage>"+
                "           <a:FullErrorMessage i:nil=\"true\" />"+
                "           <a:IsSuccess>true</a:IsSuccess>"+
                "           <a:SecurityToken>999993_310661843</a:SecurityToken>"+
                "           </ValidatePwdAndIPResult>"+
                "           </ValidatePwdAndIPResponse>"+
                "       </s:Body>\n"+
                "   </s:Envelope>\n"

        println("look at me: " + inp)
//        var intent = Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("file/*");
//        startActivityForResult(intent, YOUR_RESULT_CODE);
        val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( InputSource( StringReader(inp)))
/*
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

 */
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

    fun writeee() {

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
