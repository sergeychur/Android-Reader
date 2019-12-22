package ru.tp_project.androidreader.model.firebase
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ru.tp_project.androidreader.model.AppDb
import java.io.File
import java.lang.Exception


class FileStorage {
    private val storage = FirebaseStorage.getInstance().reference

    fun uploadFile(userId: String, uri: String, successCallback: (Uri?) -> Unit, failCallback: () -> Unit): Task<Uri> {
        val filePath = Uri.fromFile(File(uri))
        val fileRef = storage.child("internal/${userId}/${filePath.lastPathSegment}")
        return fileRef.putFile(filePath).continueWithTask {task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            fileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                successCallback(downloadUri)
            } else {
                failCallback()
            }
        }
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

    fun listFiles(isPublic: Boolean, userId: String, pageToken: String?,
                     successCallback: (MutableList<String>, MutableList<String>) -> Unit, failCallback: (Exception) -> Unit) {
        lateinit var listRef : StorageReference
        if (isPublic) {
            listRef = storage.child("internal/${userId}")
        } else {
            listRef = storage.child("public")
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