package ru.tp_project.androidreader.view

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_book_shelve.*
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.databinding.ShelveOneBookBinding
import ru.tp_project.androidreader.model.data_models.Book
import ru.tp_project.androidreader.BR
import ru.tp_project.androidreader.ReaderApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tp_project.androidreader.databinding.FragmentBookShelveBinding
import ru.tp_project.androidreader.model.data_models.Pages
import ru.tp_project.androidreader.utils.*
import ru.tp_project.androidreader.view.book_viewer.BookViewer
import ru.tp_project.androidreader.view.book_viewer.PageContentsFragment.Companion.getResizedBitmap
import ru.tp_project.androidreader.view_models.BooksShelveViewModel
import java.io.File
import java.util.*


class BookShelfFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentBookShelveBinding
    private lateinit var adapter: ListAdapter
    private var mProgressBar: ProgressBar? = null

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
        mProgressBar = view.findViewById(R.id.progress)
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
                val path = book.path
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

    private fun onUploadBook(book: Book) {
        Toast.makeText(
            activity,
            getText(R.string.upload_started),
            Toast.LENGTH_LONG
        ).show()
        val successCallback = {
            Toast.makeText(
                activity,
                getText(R.string.upload_success),
                Toast.LENGTH_LONG
            ).show()
        }

        val failCallback = {
            Toast.makeText(
                activity,
                getText(R.string.upload_fail),
                Toast.LENGTH_LONG
            ).show()
        }
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(
                activity,
                getText(R.string.no_auth),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val viewmodel = checkNotNull(viewDataBinding.viewmodel)
        viewmodel.uploadBook(book, successCallback, failCallback)
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
            ListAdapter({ bookID -> onDelete(bookID) }, { book -> onShareBook(book) },
                { book -> onUploadBook(book) })
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
            val viewModel = checkNotNull(viewDataBinding.viewmodel)
//            activity!!.runOnUiThread {
//                mProgressBar!!.visibility = View.VISIBLE
//            }
            launchBook(this, path, { }, viewModel::load)
        }
    }

//    private fun hideProgress() {
//        //mProgressBar!!.visibility = View.GONE
//    }

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
}

class ListAdapter(
    private val delete: (bookID: Int) -> Unit,
    private val shareListener: (Book) -> Unit,
    private val uploadListener: (Book) -> Unit
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
            holder.setup(pagesList[position], booksList[position], shareListener, uploadListener)
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

        fun setup(pages: Pages, book: Book, shareListener: (Book) -> Unit,
                  uploadListener: (Book) -> Unit) {
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

            itemView.findViewById<ImageButton>(R.id.bookUpload).setOnClickListener {
                uploadListener(book)
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
