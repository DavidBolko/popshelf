package com.example.popshelf.domain

data class MediaItem(
    val title: String,
    val authors: List<String>?,
    val cover:String,
    val id:String,
    val publishYear: Int
)