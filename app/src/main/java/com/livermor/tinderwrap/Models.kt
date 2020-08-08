package com.livermor.tinderwrap

import kotlinx.collections.immutable.PersistentList
import java.util.Date

// user
data class Response(
    val data: Data,
    val meta: Meta
)

data class Meta(val status: Int)

data class Data(val results: List<UserObject>)

data class UserObject(
    val user: User,
    val distance_mi: Int
)

data class User(
    val _id: String,
    val bio: String,
    val photos: List<Photo>,
    val birth_date: Date? = null
)

data class UiUser(
    val id: String,
    val bio: String,
    val photos: PersistentList<UiPhoto>,
    val birthDate: Date? = null
)

interface Estimated {
    val type: Type
    fun rate(type: Type): Estimated

    enum class Type { BAD, GOOD, NEUTRAL }
}

data class Photo(val id: String, val url: String)

data class UiPhoto(
    val id: String,
    val url: String,
    override val type: Estimated.Type = Estimated.Type.NEUTRAL
) : Estimated {
    override fun rate(type: Estimated.Type): Estimated = copy(type = type)
}

data class Bio(
    val text: String,
    override val type: Estimated.Type = Estimated.Type.NEUTRAL
) : Estimated {
    override fun rate(type: Estimated.Type): Estimated = copy(type = type)
}

// likes
data class LikeResponse(
    val status: Int,
    val match: Boolean,
    val likes_remaining: Int
)
