package com.livermor.tinderwrap

import kotlinx.collections.immutable.PersistentList

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
    val photos: List<Photo>
)

data class UiUser(
    val id: String,
    val bio: String,
    val photos: PersistentList<Photo>
)

interface Estimated {
    val isGood: Boolean
    fun rate(isGood: Boolean): Estimated
}

data class Photo(
    val id: String,
    val url: String,
    override val isGood: Boolean = false
) : Estimated {
    override fun rate(isGood: Boolean) = copy(isGood = isGood)
}

data class Bio(
    val text: String,
    override val isGood: Boolean = false
) : Estimated {
    override fun rate(isGood: Boolean): Estimated = copy(isGood = isGood)
}

// likes
data class LikeResponse(
    val status: Int,
    val match: Boolean,
    val likes_remaining: Int
)
