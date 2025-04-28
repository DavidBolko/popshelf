package com.example.popshelf.domain.useCases

import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.BookRepository
import com.example.popshelf.domain.repository.GameRepository
import com.example.popshelf.presentation.MediaType

class GetMediaDetailUseCase(private val gameRepository: GameRepository, private val bookRepository: BookRepository) {
    suspend fun execute(mediaType: MediaType, id: String): MediaItem {
        when(mediaType){
            MediaType.BOOKS -> return bookRepository.getBookDetail(id);
            MediaType.GAMES -> return gameRepository.getGameDetails(id);
            MediaType.MOVIES -> TODO()
        }
    }
}
