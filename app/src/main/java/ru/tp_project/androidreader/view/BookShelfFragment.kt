package ru.tp_project.androidreader.view

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_book_shelve.*
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.databinding.ShelveOneBookBinding
import ru.tp_project.androidreader.model.data_models.Book
import org.simpleframework.xml.core.Persister
import ru.tp_project.androidreader.BR
import ru.tp_project.androidreader.ReaderApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.databinding.FragmentBookShelveBinding
import ru.tp_project.androidreader.model.data_models.Pages
import ru.tp_project.androidreader.model.xml.BookXML
import ru.tp_project.androidreader.view.book_viewer.BookViewer
import ru.tp_project.androidreader.view.book_viewer.PageContentsFragment.Companion.getResizedBitmap
import ru.tp_project.androidreader.view.book_viewer.PagesCount
import ru.tp_project.androidreader.view.book_viewer.TextSize
import ru.tp_project.androidreader.view_models.BooksShelveViewModel
import java.io.File
import java.io.InputStream
import java.io.StringReader
import java.util.*
import kotlin.math.abs


class BookShelfFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentBookShelveBinding
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentBookShelveBinding.inflate(inflater, container, false).apply {
            viewmodel =
                ViewModelProviders.of(this@BookShelfFragment).get(BooksShelveViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun onResume() {
        super.onResume()
        setup()
    }

    private fun setup() {
        val context = ReaderApp.getInstance()
        val viewModel = viewDataBinding.viewmodel
        viewModel?.let {
            context.let { viewModel.getAll(context) }
            setupViews()
            setupAdapter()
            setupObservers(viewModel)
        }
    }

    private fun getMimeType(uri: Uri): String? {
        val mimeType: String?
        mimeType = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cr: ContentResolver = context!!.applicationContext.contentResolver
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase(Locale.ENGLISH)
            )
        }
        return mimeType
    }

    @SuppressLint("SdCardPath")
    private fun onShareBook(book: Book) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val intentShareFile = Intent(Intent.ACTION_SEND)
                val path = "/sdcard/" + book.path.split(":")[1]
                val file = File(path)
                val uri = FileProvider.getUriForFile(
                    context!!,
                    context!!.applicationContext.packageName + ".provider",
                    file
                )

                if (file.exists()) {
                    intentShareFile.type = getMimeType(uri)
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, uri)
                    intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(Intent.createChooser(intentShareFile, "Share book"))
                }
            }
        }
    }

    private fun setupViews() {
        addBook.setOnClickListener { showFileChooser() }
    }

    private fun setupObservers(viewModel: BooksShelveViewModel) {
        viewModel.data.observe(viewLifecycleOwner, Observer {
            adapter.updateBooksList(it)
        })
        viewModel.pages.observe(viewLifecycleOwner, Observer {
            adapter.updatePagesList(it)
        })
    }

    private fun setupAdapter() {
        adapter =
            ListAdapter({ bookID -> onDelete(bookID) }, { book -> onShareBook(book) })
        val layoutManager = LinearLayoutManager(activity)
        listRecyclerView.layoutManager = layoutManager
        listRecyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                layoutManager.orientation
            )
        )
        listRecyclerView.adapter = adapter
    }

    private fun onDelete(bookID: Int) {
        val viewModel = viewDataBinding.viewmodel
        viewModel!!.delete(context!!, bookID)
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
            val path = data!!.data!!
            val context =  ReaderApp.getInstance()
            val input: InputStream? = context.contentResolver.openInputStream(path)
            val inputAsString = input!!.bufferedReader().use { it.readText() }
            val size = stringSize(inputAsString)

            val book = loadBookXML(inputAsString)
            if (book == null) {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle(getString(R.string.wrong_file_title))
                builder.setMessage(getString(R.string.wrong_file_message))
                builder.setPositiveButton(android.R.string.yes) { _, _ ->
                    Toast.makeText(
                        activity,
                        android.R.string.yes, Toast.LENGTH_SHORT
                    ).show()
                }
                builder.show()
            } else {
                val viewModel = viewDataBinding.viewmodel
                val bookBD = xmlToDB(book, path.path!!, size)
                val pc = PagesCount{pages ->
                    viewModel!!.load(context, bookBD, pages) { id ->
                        bookBD.id = id.toInt()
                        showContent(context, bookBD)
                    }
                }
                pc.execute(createTextSize(book.body.section.joinToString(""), 0))
            }
        }
    }

    private fun createTextSize(content: String, bookID : Int): TextSize {
        val textviewPage = layoutInflater.inflate(
            R.layout.book_viewer_fragment, null,
            false
        ) as ViewGroup
        val layout = textviewPage.findViewById(R.id.mText) as LinearLayout
        val view = layout.findViewById(R.id.text) as TextView

        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        val verticalMargin = resources.getDimension(R.dimen.activity_vertical_margin)
        val paint = view.paint

        //Working Out How Many Lines Can Be Entered In The Screen
        val fm = paint.fontMetrics
        var textHeight = fm.top - fm.bottom
        textHeight = abs(textHeight)

        val maxLineCount = ((height - verticalMargin) / textHeight).toInt()

        return TextSize (paint, width, maxLineCount, content, bookID)
    }

    private fun xmlToDB(bookXML: BookXML, path: String, size: String): Book {
        var str = ""
        for (s in bookXML.body.section) {
            str += s
        }
        return Book(
            0, bookXML.description.titleInfo.book_title,
            bookXML.binary, bookXML.description.titleInfo.author.first_name +
                    bookXML.description.titleInfo.author.last_name,
            bookXML.description.titleInfo.date,
            bookXML.description.publishInfo.publisher,
            bookXML.description.titleInfo.genre,
            size, "fb2", 0f, str, path, 0, 0, 0
        )
    }

    private fun stringSize(raw: String): String {
        var size = raw.length.toFloat()
        var typeId = 0
        var type = ""
        size /= 1024
        while (size > 1024) {
            size /= 1024
            typeId++
        }
        when (typeId) {
            0 -> type = getString(R.string.kb)
            1 -> type = getString(R.string.mb)
            2 -> type = getString(R.string.gb)

        }
        val sizeString = "%.2f".format(size)
        return sizeString+" $type"
    }


    companion object {
        private fun setToIntent(intent: Intent, book: Book) {
            intent.putExtra("book", book)
        }

        fun showContent(context: Context, book: Book) {
            val intent = Intent(context, BookViewer::class.java)
            setToIntent(intent, book)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(context, intent, null)
        }
    }

    private fun getContent(xml: String): String? {
        val start = xml.indexOf("<section>") + "<section>".length
        val end = xml.indexOf("</section>")
        if (start >= end) {
            return null
        }
        return xml.substring(start..end)
    }

    // addContentToModel add rows to BookXML. If no rows, add one row "No content"
    private fun addContentToModel(book: BookXML, content: String?): BookXML {
        val rows: MutableList<String> = mutableListOf()
        if (content != null) {
            val strings = content.split("</p>")
            val s = 4
            for (str in strings) {
                if (str.length < s) {
                    continue
                }
                rows.add(str.substring(s))
            }
        }
        if (rows.size > 0) {
            book.body.section = rows
        } else {
            book.body.section = listOf("No content")
        }
        return book
    }

    private fun loadBookXML(xml: String): BookXML? {
        val reader = StringReader(xml)
        val serializer = Persister()
        var book: BookXML
        try {
            book = serializer.read(BookXML::class.java, reader, false)
        } catch (e: Exception) {
            return null
        }

        val content = getContent(xml)
        book = addContentToModel(book, content)
        return book
    }
}

class ListAdapter(
    private val delete: (bookID: Int) -> Unit,
    private val shareListener: (Book) -> Unit
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    private var booksList: List<Book> = emptyList()
    private var pagesList: List<Pages> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflatter = LayoutInflater.from(parent.context)
        val binding = ShelveOneBookBinding.inflate(inflatter, parent, false)
        return ListViewHolder(binding.root, binding, parent.context, delete)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Log.d("sizes", ""+booksList.size + " " + pagesList.size)
        if (position < booksList.size && booksList.size == pagesList.size) {
            holder.setup(pagesList[position], booksList[position], shareListener)
        }
    }

    fun updateBooksList(booksList: List<Book>) {
        this.booksList = booksList
        notifyDataSetChanged()
    }

    fun updatePagesList(pagesList: List<Pages>) {
        this.pagesList = pagesList
        notifyDataSetChanged()
    }


    override fun getItemCount() = booksList.size


    class ListViewHolder(
        itemView: View,
        private val dataBinding: ViewDataBinding,
        val context: Context,
        var delete: (bookID: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        private var itemData: Book? = null

        fun setup(pages: Pages, book: Book, shareListener: (Book) -> Unit) {
            val imageView = itemView.findViewById(R.id.bookPreview) as ImageView

            itemData = book
            val imageBytes = Base64.decode(book.photo, 0)
            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            if (image != null) {
                imageView.setImageBitmap(getResizedBitmap(image, 300, 480))
            } else {
                imageView.setImageResource(R.drawable.nocover)
            }

            val seekBar = itemView.findViewById(R.id.bookProgress) as ProgressBar

            seekBar.max = pages.pageCount
            seekBar.progress = pages.pageCurrent

            itemView.findViewById<ImageButton>(R.id.bookShare).setOnClickListener {
                shareListener(book)
            }


            val deleter = itemView.findViewById<ImageButton>(R.id.bookDelete)
            deleter.setOnClickListener {
                delete(itemData!!.id)
            }

            dataBinding.setVariable(BR.book, itemData)
            dataBinding.executePendingBindings()
        }

        override fun onClick(v: View) {
            BookShelfFragment.showContent(context, itemData!!)
        }
    }
}
