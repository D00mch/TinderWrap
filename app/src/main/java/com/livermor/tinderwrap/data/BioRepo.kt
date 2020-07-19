package com.livermor.tinderwrap.data

import android.content.Context
import android.os.Environment
import android.util.Log
import com.livermor.tinderwrap.Bio
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class BioRepository(private val context: Context) {

    fun saveBio(bio: Bio) {

        val externalPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val storageDir = File("$externalPath/Tinder")
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val fileName = if (bio.isGood) "good.txt" else "awful.txt"
            val textFile = File(storageDir, fileName)
            try {
                BufferedWriter(FileWriter(textFile, true)).use { bw ->
                    bw.write(bio.text)
                    bw.newLine()
                    bw.write(SEPARATOR)
                    bw.newLine()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Log.i(BioRepository::class.java.simpleName, "saveText: unsuccessful")
        }
    }

    companion object {
        private const val SEPARATOR = "@*@*@"
    }
}