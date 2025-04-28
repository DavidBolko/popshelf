package com.example.popshelf.domain

import com.example.popshelf.presentation.MediaType

data class MediaItem(
    val title: String,
    val author: String,
    val cover:String,
    val id:String,
    val publishYear: Int,
    val desc: String,
    val mediaType: MediaType,
    var rating: Int = 0
)