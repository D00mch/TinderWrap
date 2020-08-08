package com.livermor.tinderwrap.factory

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object GlideFactory {
    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
}

