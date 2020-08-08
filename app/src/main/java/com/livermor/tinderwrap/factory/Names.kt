package com.livermor.tinderwrap.factory

import com.livermor.tinderwrap.Bio
import com.livermor.tinderwrap.Estimated
import com.livermor.tinderwrap.UiPhoto

interface Names {
    fun get(photo: UiPhoto, drop: Int = 0): String
    fun get(bio: Bio): String
}

object NamesImpl : Names {

    override fun get(photo: UiPhoto, drop: Int): String {
        val prefix = photo.type.prefix()
        return "$prefix${photo.id.drop(n = drop)}.jpg"
    }

    override fun get(bio: Bio): String = when (bio.type) {
        Estimated.Type.BAD -> "awful.txt"
        Estimated.Type.NEUTRAL -> "neutral.txt"
        Estimated.Type.GOOD -> "good.txt"
    }
}

