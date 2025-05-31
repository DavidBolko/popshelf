package com.example.popshelf.data.repository

import android.util.Log
import com.example.popshelf.data.remote.BookApi
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.dao.MovieDao
import com.example.popshelf.data.local.dao.ShelfItemDao
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.MovieEntity
import com.example.popshelf.data.local.entity.ShelfItemEntity
import com.example.popshelf.data.remote.tmdbApiService
import com.example.popshelf.data.toMediaItem
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.BookRepository
import com.example.popshelf.domain.repository.TvRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TvRepositoryImpl(private val tmdbApi: tmdbApiService, private val movieDao: MovieDao): TvRepository {
    override suspend fun searchMovies(title: String, page: Int): List<MediaItem> {
        val results: List<MovieEntity> = movieDao.findByName(title)

        if (results.isNotEmpty()) {
            Log.d("QueryPop", "Local request")
            return results.map { it.toMediaItem() }
        }

        val response = tmdbApi.searchMovies(title.trim(), page)
        Log.d("QueryPop", response.results.toString())

        val mediaItems = response.results.map { it.toMediaItem() }
        movieDao.insertAll(mediaItems.map {
            MovieEntity(it.id, it.title, it.author, it.cover, it.publishYear, it.desc)
        })

        return mediaItems
    }


    override suspend fun getShowDetails(id: String): MediaItem {
        TODO("Not yet implemented")
    }

}