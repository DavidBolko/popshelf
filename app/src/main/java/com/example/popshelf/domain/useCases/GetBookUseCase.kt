package com.example.popshelf.domain.useCases

import com.example.popshelf.data.bookApi
import com.example.popshelf.data.BookSearchResponse
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.BookRepository


class GetBookUseCase(private val bookRepository: BookRepository) {
    suspend fun execute(query: String): List<MediaItem>{
        return bookRepository.searchBooks(query)
    }
}
