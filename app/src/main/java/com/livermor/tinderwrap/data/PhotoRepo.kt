package com.livermor.tinderwrap.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.livermor.tinderwrap.UiPhoto
import com.livermor.tinderwrap.factory.Names
import java.io.File
import java.io.FileOutputStream

private const val WIDTH = 320
private const val HEIGHT = 400

class PhotoRepository(
    private val context: Context,
    private val names: Names
) {

    fun savePhotos(photos: List<UiPhoto>) {
        photos.forEach { photo ->
            Glide.with(context)
                .asBitmap()
                .load(photo.url)
                .into(object : CustomTarget<Bitmap>(WIDTH, HEIGHT) {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        Log.i(PhotoRepository::class.java.simpleName, "onLoadCleared")
                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        Log.i(PhotoRepository::class.java.simpleName, "about to save image")
                        saveImage(resource, names.get(photo), RepoConst.FOLDER_WITH_NEUTRAL)
                    }
                })
        }
    }

    private fun saveImage(image: Bitmap, fileName: String, folder: String): String? {
        var savedImagePath: String? = null
        val externalPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDir = File("$externalPath/$folder")
        Log.i(PhotoRepository::class.java.simpleName, "saveImage: storageDir $storageDir")
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, fileName)
            savedImagePath = imageFile.absolutePath
            try {
                FileOutputStream(imageFile).use { fOut ->
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Log.i(PhotoRepository::class.java.simpleName, "saveImage: unsuccessful")
        }
        return savedImagePath
    }
}