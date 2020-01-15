package ru.tp_project.androidreader

import android.annotation.SuppressLint
import android.app.Application
import ru.tp_project.androidreader.model.AppDb

class ReaderApp : Application() {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @SuppressLint("SdCardPath")
    val DOWNLOAD_PATH = "/sdcard/Download"
    init {
        INSTANCE = this
    }

    companion object {
        private var INSTANCE: Application? = null

        fun getInstance(): ReaderApp {
            return INSTANCE as ReaderApp
        }
    }
    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!
        AppDb.getInstance(this)
    }
}