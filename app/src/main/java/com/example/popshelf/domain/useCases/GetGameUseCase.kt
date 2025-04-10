package com.example.popshelf.domain.useCases

import com.example.popshelf.data.gameApi
import com.example.popshelf.data.GameSearchResponse
import com.example.popshelf.BuildConfig
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.GameRepository

class GetGameUseCase(private val gameRepository: GameRepository) {
    suspend fun execute(query: String): List<MediaItem> {
        return gameRepository.findGames(query);
    }
}
