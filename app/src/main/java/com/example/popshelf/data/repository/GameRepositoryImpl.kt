package com.example.popshelf.data.repository

import com.example.popshelf.BuildConfig
import com.example.popshelf.data.GameApi
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.GameRepository

class GameRepositoryImpl(private val gameApi: GameApi): GameRepository {
    override suspend fun findGames(title: String): List<MediaItem> {
        val results = gameApi.findGames(query = title, false, apiKey = BuildConfig.RAWG_KEY)
        return results.results.map { game ->
            MediaItem(game.name, "", game.background_image, "Asdasd", 1943)
        }
    }
}