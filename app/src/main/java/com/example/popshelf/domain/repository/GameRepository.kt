package com.example.popshelf.domain.repository

import com.example.popshelf.domain.MediaItem

interface GameRepository {
    suspend fun findGamesByTitle(title: String): List<MediaItem>;
    suspend fun getGameDetails(id: String): MediaItem
}