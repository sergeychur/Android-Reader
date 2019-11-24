package ru.tp_project.androidreader.utils

import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.os.Parcelable.Creator
import android.content.Intent
import java.nio.file.Files.exists
import java.nio.file.Files.isDirectory
import android.R.attr.entries
import java.nio.file.Files.delete
import android.app.IntentService
import android.content.Context
import android.os.*
import android.util.Log
import java.io.*
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.nio.file.Files.isDirectory
import java.util.zip.ZipInputStream
/*

class Decompress(private val _zipFile: String, private val _location: String) {

    init {

        _dirChecker("")
    }

    fun unzip() {
        try {
            val fin = FileInputStream(_zipFile)
            val zin = ZipInputStream(fin)

            val b = ByteArray(1024)

            var ze: ZipEntry? = null
            while (ze != null) {
                ze = zin.getNextEntry()
                Log.v("Decompress", "Unzipping " + ze!!.name)

                if (ze.isDirectory) {
                    _dirChecker(ze.name)
                } else {
                    val fout = FileOutputStream(_location + ze.name)

                    val `in` = BufferedInputStream(zin)
                    val out = BufferedOutputStream(fout)

                    var n: Int
                    while ((n = `in`.read(b, 0, 1024)) >= 0) {
                        out.write(b, 0, n)
                    }

                    zin.closeEntry()
                    out.close()
                }

            }
            zin.close()
        } catch (e: Exception) {
            Log.e("Decompress", "unzip", e)
        }

    }

    private fun _dirChecker(dir: String) {
        val f = File(_location + dir)

        if (!f.isDirectory) {
            f.mkdirs()
        }
    }
}

*/