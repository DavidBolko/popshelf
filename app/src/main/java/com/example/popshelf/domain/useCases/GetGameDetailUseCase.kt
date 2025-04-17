package com.example.popshelf.domain.useCases

import com.example.popshelf.data.gameApi
import com.example.popshelf.data.GameSearchResponse
import com.example.popshelf.BuildConfig
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.GameRepository

class GetGameDetailUseCase(private val gameRepository: GameRepository) {
    suspend fun execute(id: String): MediaItem {
        return gameRepository.getGameDetails(id);
    }
}
