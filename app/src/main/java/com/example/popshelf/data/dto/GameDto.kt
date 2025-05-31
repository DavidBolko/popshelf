package com.example.popshelf.data.dto


data class GameDto(
    val id: String,
    val title: String,
    val authors: String,
    val cover: String,
    val released: Int,
    val summary: String,
    val updatedAt: Long,
    val rating: Int?,
    val comment: String?,
    val shelfId: Int = 0,
    val status: String?

)