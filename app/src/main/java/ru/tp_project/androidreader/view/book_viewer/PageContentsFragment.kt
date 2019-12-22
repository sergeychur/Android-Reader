package ru.tp_project.androidreader.view.book_viewer

import android.graphics.*
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ru.tp_project.androidreader.MainActivity
import ru.tp_project.androidreader.R

class PageContentsFragment(var mPhoto:String) : PageContentsFragmentBase() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.book_viewer_fragment, container, false) as ViewGroup

        val layout = rootView.findViewById(R.id.mText) as LinearLayout
        val contentTextView = layout.findViewById(R.id.text) as TextView

        val imageView = layout.findViewById(R.id.image) as ImageView

        val imageBytes = Base64.decode(mPhoto, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        imageView.setImageBitmap(getResizedBitmap(image, (image.width*1.5f).toInt(), (image.height*1.5f).toInt()))

        val contents = (activity as BookViewer).getContents(pageNumber)
        contentTextView.setText(contents)
        contentTextView.movementMethod = ScrollingMovementMethod()
        return rootView
    }

    fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)

        val scaleX = newWidth / bitmap.width.toFloat()
        val scaleY = newHeight / bitmap.height.toFloat()
        val pivotX = 0f
        val pivotY = 0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY)

        val canvas = Canvas(resizedBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, Paint(Paint.FILTER_BITMAP_FLAG))

        return resizedBitmap
    }

}
