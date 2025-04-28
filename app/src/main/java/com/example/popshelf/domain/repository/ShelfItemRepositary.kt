package com.example.popshelf.domain.repository

import com.example.popshelf.domain.MediaItem


interface ShelfItemRepositary {
    suspend fun addShelfItem(id: String, mediaType: String, status: String, rating: Int, comment:String = "", shelf:String)
    suspend fun getAllBooks(): List<MediaItem>
    suspend fun getShelfItems(id:Int): List<MediaItem>
}