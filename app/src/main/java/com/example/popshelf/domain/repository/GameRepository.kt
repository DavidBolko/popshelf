package com.example.popshelf.domain.repository

import com.example.popshelf.domain.MediaItem

interface GameRepository {
    suspend fun findGames(title: String): List<MediaItem>;
}