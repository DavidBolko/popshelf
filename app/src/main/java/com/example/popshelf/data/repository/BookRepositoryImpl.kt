package com.example.popshelf.data.repository

import android.util.Log
import com.example.popshelf.data.Book
import com.example.popshelf.data.BookApi
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.entity.BookEntity
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
        if(results.isEmpty()) {
            val results = bookApi.searchBooks(title).docs
            Log.d("QueryPop", "API Request")
            val mediaItems = results.map { book ->
                MediaItem(
                    title = book.title,
                    author = book.author_name[0],
                    cover = book.cover_i.toString(),
                    id = book.key,
                    publishYear = book.first_publish_year
                )
            }
            withContext(Dispatchers.IO) {
                bookDao.insertAll(mediaItems.map { BookEntity(it.id.substringAfterLast('/'), it.title, it.author, it.cover, it.publishYear) })
            }
            return mediaItems
        }
        val mediaItems = results.map { book ->
            MediaItem(
                title = book.title,
                author = book.author,
                cover = book.cover,
                id = book.id,
                publishYear = book.publishYear
            )
        }
        Log.d("QueryPop", "Local request")
        return mediaItems
    }

    override suspend fun getBookDetail(id: String): MediaItem {
        val result: BookEntity
        withContext(Dispatchers.IO) {
            result = bookDao.findById(id)
        }
        return MediaItem(result.title, result.author, result.cover, result.id, result.publishYear);
    }
}