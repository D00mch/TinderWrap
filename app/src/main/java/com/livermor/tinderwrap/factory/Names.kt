package com.livermor.tinderwrap.factory

import com.livermor.tinderwrap.Bio
import com.livermor.tinderwrap.Estimated
import com.livermor.tinderwrap.UiPhoto

interface Names {
    fun get(photo: UiPhoto): String
    fun get(bio: Bio): String
}

object NamesImpl : Names {

    override fun get(photo: UiPhoto): String {
        val prefix = photo.type.prefix()
        return "$prefix${photo.id}.jpg"
    }

    override fun get(bio: Bio): String = when (bio.type) {
        Estimated.Type.BAD -> "awful.txt"
        Estimated.Type.NEUTRAL -> "neutral.txt"
        Estimated.Type.GOOD -> "good.txt"
    }
}

