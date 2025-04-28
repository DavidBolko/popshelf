package com.example.popshelf.data.dto

import androidx.room.Embedded

data class Book(
    val title: String,
    val author_name: List<String>,
    val cover_i: Int?,
    val key: String,
    val first_publish_year: Int
)

data class WorkDetailResponse(
    val title: String?,
    val description: Any?,
    val covers: List<Int>?,
    val subjects: List<String>?
)

data class BookSearchResponse(
    val docs: List<Book> = emptyList()
)

data class BookDto(
    val id: String,
    val title: String,
    val author: String,
    val cover: String,
    val publishYear: Int,
    val desc: String?,
    val rating: Int?
)