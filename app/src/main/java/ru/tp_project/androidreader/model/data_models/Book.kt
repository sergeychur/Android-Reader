package ru.tp_project.androidreader.model.data_models

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.tp_project.androidreader.R
import ru.tp_project.androidreader.view.book_viewer.PageContentsFragment

@Entity(tableName = "book")
data class Book(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val name: String,
    val photo: String,
    val author: String,

    val date: String,
    val source: String,
    val genre: String,

    val size: String,
    var format: String,
    val progress: Float,
    val path: String,

    var pages: Int,
    var words: Long,
    var currPage: Int
) : java.io.Serializable {

    fun setImageToImageView(imageView: ImageView) {
        val imageBytes = Base64.decode(photo, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        if (image != null) {
            imageView.setImageBitmap(image)
        } else {
            imageView.setImageResource(R.drawable.nocover)
        }
    }
}