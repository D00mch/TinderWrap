package com.livermor.tinderwrap.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.livermor.tinderwrap.Photo
import com.livermor.tinderwrap.databinding.ItemPhotoBinding

class PhotoAdapter() : ViewBindingDelegateAdapter<Photo, ItemPhotoBinding>(ItemPhotoBinding::inflate) {

    @SuppressLint("ClickableViewAccessibility")
    override fun ItemPhotoBinding.onBind(item: Photo) {
        Log.i(PhotoAdapter::class.java.simpleName, "onBind: trying to load image")
        val context = ivPhoto.context
        Glide
            .with(context)
            .load(item.url)
            .apply(requestOptions)
            .into(ivPhoto)

        vIndicator.setBackgroundColor(
            if (item.isGood) Color.GREEN else Color.RED
        )
    }

    override fun isForViewType(item: Any) = item is Photo
    override fun Photo.getItemId() = id

    companion object {
        private val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
    }
}