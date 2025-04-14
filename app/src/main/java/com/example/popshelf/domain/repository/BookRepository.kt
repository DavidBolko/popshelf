package com.example.popshelf.domain.repository

import com.example.popshelf.domain.MediaItem

interface BookRepository {
    suspend fun searchBooks(title: String): List<MediaItem>;
    suspend fun getBookDetail(id: String): MediaItem
}