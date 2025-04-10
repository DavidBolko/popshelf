package com.example.popshelf.data.repository

import com.example.popshelf.data.BookApi
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.BookRepository

class BookRepositoryImpl(private val bookApi: BookApi): BookRepository{
    override suspend fun searchBooks(title: String): List<MediaItem> {
        val results = bookApi.searchBooks(title)
        return results.docs.map { book ->
            MediaItem(book.title, book.author_name, book.cover_i.toString(), book.key.toString(), book.first_publish_year)
        }
    }
}