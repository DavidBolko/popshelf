package com.example.popshelf.domain.repository

import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.Shelf


interface ShelfRepositary {
    suspend fun createShelf(name: String, color:String)
    suspend fun getAllShelves(isSystem:Boolean): List<Shelf>
    suspend fun createDefaultShelves()
}