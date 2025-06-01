package com.example.popshelf.data.dto


data class MediaDto(
    val id: String,
    val title: String,
    val author: String,
    val cover: String,
    val released: Int = 0,
    val desc: String = "",
    val genres: String? = "",
    val updatedAt: Long = 0L,
    val rating: Int = 0,
    val comment: String? = "",
    val shelfId: Int = 0,
    val status: String? = "",
    val mediaType: String
)
