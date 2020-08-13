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
import com.livermor.tinderwrap.factory.GlideFactory
import com.livermor.tinderwrap.factory.Names
import com.livermor.tinderwrap.factory.toType
import java.io.File
import java.io.FileOutputStream

private const val WIDTH = 320
private const val HEIGHT = 400
private const val TAG = "PhotoRepository"

class PhotoRepository(
    private val context: Context,
    private val names: Names
) {
    private val externalPath by lazy {
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()
    }

    fun savePhotos(photos: List<UiPhoto>) = savePhotos(photos, folder = RepoConst.FOLDER)
    fun saveCheckedPhotos(photos: List<UiPhoto>) = savePhotos(photos, folder = RepoConst.FOLDER_WITH_NEUTRAL)

    fun movePhotosToChecked(photos: List<UiPhoto>) {
        photos.forEach { photo ->
            moveFile(
                oldName = photo.id,
                newName = names.get(photo, drop = 1),
                oldDir = RepoConst.FOLDER,
                newDir = RepoConst.FOLDER_WITH_NEUTRAL
            )
        }
    }

    fun deletePhotos(photos: List<UiPhoto>) {
        photos.forEach { photo ->
            val uri = photo.url
            val file = File(uri)
            if (file.exists()) {
                val deleted = file.delete()
                Log.d(TAG, "deletePhotos: $photo deleted: $deleted")
            } else {
                Log.w(TAG, "deletePhotos: $photo doesn't exist")
            }
        }
    }

    fun getPhotos(count: Int, predicate: (UiPhoto) -> Boolean): List<UiPhoto> {
        val path = "$externalPath/${RepoConst.FOLDER}"
        Log.d(TAG, "getPhotos: path $path")
        val directory = File(path)
        val files = directory.listFiles()
        Log.d(TAG, "getPhotos: size ${files?.size ?: 0}")
        return files
            ?.map { file ->
                UiPhoto(id = file.name, url = file.absolutePath, type = file.name.toType())
            }
            ?.filter { predicate(it) }
            ?.take(count)
            ?: emptyList()
    }

    private fun savePhotos(photos: List<UiPhoto>, folder: String) = photos.forEach { photo ->
        Glide.with(context)
            .asBitmap()
            .load(photo.url)
            .apply(GlideFactory.requestOptions)
            .into(object : CustomTarget<Bitmap>(WIDTH, HEIGHT) {
                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.i(PhotoRepository::class.java.simpleName, "onLoadCleared")
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Log.i(PhotoRepository::class.java.simpleName, "about to save image")
                    saveImage(resource, names.get(photo), folder)
                }
            })
    }

    private fun saveImage(image: Bitmap, fileName: String, folder: String): String? {
        var savedImagePath: String? = null
        val storageDir = File("$externalPath/$folder")
        Log.d(TAG, "saveImage: storageDir $storageDir")
        if (storageDir.exists() || storageDir.mkdirs()) {
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
            Log.i(TAG, "saveImage: unsuccessful")
        }
        return savedImagePath
    }

    private fun moveFile(oldName: String, oldDir: String, newDir: String, newName: String): Boolean {
        val oldDir = File("$externalPath/$oldDir")
        val newDir = File("$externalPath/$newDir")
        val checkOldExist = oldDir.exists() || oldDir.mkdirs()
        val checkNewExist = newDir.exists() || newDir.mkdirs()
        if (checkOldExist && checkNewExist) {
            val file = File(oldDir, oldName)
            return try {
                val result = file.renameTo(File(newDir, newName))
                Log.i(TAG, "moveFile: name $oldName, moved $result")
                result
            } catch (e: Exception) {
                Log.e(TAG, "moveFile: error $e")
                return false
            }
        } else {
            Log.i(TAG, "moveFile: unsuccessful")
            return false
        }
    }
}