package com.example.popshelf.data.repository

import android.util.Log
import com.example.popshelf.data.remote.BookApi
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.dao.ShelfItemDao
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.ShelfItemEntity
import com.example.popshelf.data.toMediaItem
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepositoryImpl(private val bookApi: BookApi, private val bookDao: BookDao, private val shelfItemDao: ShelfItemDao): BookRepository{
    override suspend fun searchBooks(title: String): List<MediaItem> {
        val results: List<BookEntity> = bookDao.findByName(title)

        if (results.isNotEmpty()) {
            Log.d("QueryPop", "Local request")
            return results.map { it.toMediaItem() }
        }

        val apiResults = bookApi.searchBooks(title.trim()).docs

        Log.d("QueryPop", apiResults.toString())

        val mediaItems = apiResults.map { it.toMediaItem() }
        bookDao.insertAll(mediaItems.map {
            BookEntity(it.id, it.title, it.author, it.cover, it.publishYear, it.desc)
        })

        return mediaItems
    }

    override suspend fun getBookDetail(id: String): MediaItem = withContext(Dispatchers.IO) {
        val info = bookApi.getWorkDetail(id)

        val desc = when (val raw = info.description) {
            null -> "Description not available"
            else -> {
                val text = raw.toString()
                if (text.contains("value=")) text.substringAfter("value=") else text
            }
        }
        bookDao.updateDesc(id, desc)
        val bookWithRating = bookDao.findById(id)
        Log.d("dad", bookWithRating.toString())
        return@withContext bookWithRating.toMediaItem()
    }
}