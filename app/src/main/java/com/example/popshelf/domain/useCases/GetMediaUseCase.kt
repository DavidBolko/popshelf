package com.example.popshelf.domain.useCases

import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.BookRepository
import com.example.popshelf.domain.repository.GameRepository
import com.example.popshelf.presentation.MediaType


class GetMediaUseCase(private val bookRepository: BookRepository, private val gameRepository: GameRepository) {
    suspend fun execute(mediaType: MediaType, query: String): List<MediaItem>{
        when(mediaType){
            MediaType.BOOKS -> return bookRepository.searchBooks(query)
            MediaType.GAMES -> return gameRepository.findGamesByTitle(query)
            MediaType.MOVIES -> TODO()
        }
    }
}
