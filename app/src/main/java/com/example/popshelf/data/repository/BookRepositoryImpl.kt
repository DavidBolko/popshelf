package com.example.popshelf.data.repository

import android.util.Log
import com.example.popshelf.data.BookApi
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.toMediaItem
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepositoryImpl(private val bookApi: BookApi, private val bookDao: BookDao): BookRepository{
    override suspend fun searchBooks(title: String): List<MediaItem> {
        val results: List<BookEntity>
        withContext(Dispatchers.IO) {
            results = bookDao.findByName(title)
        }

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

    override suspend fun getBookDetail(id: String): MediaItem {
        val result: BookEntity
        withContext(Dispatchers.IO) {
            val info = bookApi.getWorkDetail(id)
            var desc = info.description
            desc = when {
                desc == null -> "Description not available"
                desc.toString().contains("value=") -> desc.toString().substringAfter("value=")
                else -> desc.toString()
            }
            bookDao.updateDesc(id, desc)

            result = bookDao.findById(id)
        }
        return result.toMediaItem()
    }
}