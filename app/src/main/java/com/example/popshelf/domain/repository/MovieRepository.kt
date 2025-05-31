package com.example.popshelf.domain.repository

import com.example.popshelf.domain.MediaItem

interface TvRepository {
    suspend fun searchMovies(title: String, page: Int = 1): List<MediaItem>;
    suspend fun getShowDetails(id: String): MediaItem
}