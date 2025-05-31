package com.example.popshelf.data.dto


data class MovieDto(
    val id: String,
    val title: String,
    val author: String,
    val cover:String,
    val publishYear: Int,
    val desc: String?,
    val genres: String,
    val updatedAt: Long,
    val rating: Int?,
    val comment: String?,
    val shelfId: Int = 0,
    val status: String?

)