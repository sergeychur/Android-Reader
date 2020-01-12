package ru.tp_project.androidreader.model.firebase
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileNotFoundException
import kotlin.Exception
import kotlin.NullPointerException


@Suppress("UNUSED_VARIABLE")
class FileStorage {
    private val storage = FirebaseStorage.getInstance().reference

    fun uploadFile(userId: String?, book: File, successCallback: () -> Unit, failCallback: (Exception) -> Unit) {
        if (userId == null) {
            try {
                throw NullPointerException("No userId")
            } catch (e: NullPointerException) {
                failCallback(e)
            }
        }
        if (!book.exists()) {
            try {
                throw FileNotFoundException("File Not Found")
            } catch (e: FileNotFoundException) {
                failCallback(e)
            }
        }
        val filePath = Uri.fromFile(book)
        val fileRef = storage.child("internal/${userId}/${filePath.lastPathSegment}")
        fileRef.putFile(filePath)
            .addOnSuccessListener { successCallback() }
      // TODO(smbdy): comment addOnFailureListener if keeps falling
            .addOnFailureListener { failCallback(it) }
    }

    fun downloadFile(url: String, destination: File,
                     successCallback: () -> Unit, failCallback: (Exception) -> Unit) {
        val fileRef = storage.child(url)
        fileRef.getFile(destination).addOnSuccessListener {
            successCallback()
        }.addOnFailureListener {
            failCallback(it)
        }
    }

    fun listFiles(isPublic: Boolean, userId: String?, pageToken: String?,
                     successCallback: (MutableList<String>, MutableList<String>) -> Unit, failCallback: (Exception) -> Unit) {
        lateinit var listRef : StorageReference
        if (!isPublic) {
            if (userId == null) {
                failCallback(Exception("no user id"))
            }
            listRef = storage.child("internal/${userId}")
        } else {
            listRef = storage.child("public/")
        }
        val listPageTask = if (pageToken != null) {
            listRef.list(100, pageToken)
        } else {
            listRef.list(100)
        }

        listPageTask
            .addOnSuccessListener { listResult ->
                val prefixes = listResult.prefixes
                val items = listResult.items
                val fileNames = mutableListOf<String>()
                val filePathes = mutableListOf<String>()
                items.forEach { fileNames.add(it.name)
                    filePathes.add(it.path)
                }
                successCallback(fileNames, filePathes)
            }
            .addOnFailureListener {
                failCallback(it)
            }
    }

    fun deleteFile(url: String, successCallback: () -> Unit, failCallback: (Exception) -> Unit) {
        val fileRef = storage.child(url).delete().addOnSuccessListener {
            successCallback()
        }.addOnFailureListener {
            failCallback(it)
        }
    }

    companion object {
        private var INSTANCE: FileStorage? = null
        fun getInstance() : FileStorage {
            if (INSTANCE == null) {
                INSTANCE = FileStorage()
            }
            return INSTANCE!!
        }
    }
}
