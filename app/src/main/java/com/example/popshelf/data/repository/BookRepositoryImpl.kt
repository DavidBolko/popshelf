package com.example.popshelf.data.repository

import com.example.popshelf.data.remote.BookApi
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.toMediaItem
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.NetworkStatusProvider
import com.example.popshelf.domain.repository.BookRepository

class BookRepositoryImpl(private val bookApi: BookApi, private val bookDao: BookDao, private val networkStatusProvider: NetworkStatusProvider): BookRepository{
    override suspend fun getBooksByQuery(query: String, page: Int): List<MediaItem> {
        val trimmedTitle = query.trim()

        return if (!networkStatusProvider.isOnline() || query.isEmpty()) {
            bookDao.findByName(trimmedTitle).map { it.toMediaItem() }
        } else {
            val apiResults = bookApi.searchBooks(trimmedTitle, page = page, limit = 20)

            val mediaItems = apiResults.docs.map { it.toMediaItem() }
            bookDao.insertAll(mediaItems.map {
                BookEntity(it.id, it.title, it.author, it.cover, it.released, it.desc)
            })
            return mediaItems
        }

    }


    override suspend fun getBookDetail(id: String): MediaItem {
        val book = bookDao.findById(id)
        val dayMillis = 24 * 60 * 60 * 1000L
        val shouldFetch = networkStatusProvider.isOnline() && (System.currentTimeMillis() - book.updatedAt > dayMillis)

        if (shouldFetch) {
            val info = bookApi.getWorkDetail(id)

            val desc = when (val raw = info.description) {
                null -> "Description not available"
                else -> {
                    val text = raw.toString()
                    if (text.contains("value=")) text.substringAfter("value=") else text
                }
            }

            bookDao.insert(BookEntity(
                id = id,
                title = book.title,
                author = book.author,
                cover = book.cover,
                released = book.released,
                desc = desc,
                updatedAt = System.currentTimeMillis()
            ))
        }

        return bookDao.findById(id).toMediaItem()
    }
}