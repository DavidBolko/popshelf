package com.example.popshelf.data.dto


data class BookDto(
    val id: String,
    val title: String,
    val author: String,
    val cover: String,
    val publishYear: Int,
    val desc: String?,
    val rating: Int?,
    val comment: String?,
    val shelfId: Int = 0,
    val status: String?
)