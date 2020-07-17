package com.livermor.tinderwrap

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
    val photos: List<Photo>
)

data class Photo(
    val id: String,
    val url: String
)
