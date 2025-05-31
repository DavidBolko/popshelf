package com.example.popshelf.data.repository

import android.util.Log
import com.example.popshelf.data.local.dao.MovieDao
import com.example.popshelf.data.local.entity.MovieEntity
import com.example.popshelf.data.remote.TmdbApiService
import com.example.popshelf.data.remote.tmdbDetailResponse
import com.example.popshelf.data.toMediaItem
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.NetworkStatusProvider
import com.example.popshelf.domain.repository.MovieRepository

class MovieRepositoryImpl(private val tmdbApi: TmdbApiService, private val movieDao: MovieDao, private val networkStatusProvider: NetworkStatusProvider): MovieRepository {
    override suspend fun getMoviesByQuery(query: String, page: Int): List<MediaItem> {
        return if (!networkStatusProvider.isOnline() || query.isEmpty()) {
            val results: List<MovieEntity> = movieDao.findByName(query)
            results.map { it.toMediaItem() }
        } else {
            val trimmedTitle = query.trim()

            val movieResponse = tmdbApi.searchMovies(trimmedTitle, page)
            val tvResponse = tmdbApi.searchTvShows(trimmedTitle, page)

            val movies = movieResponse.results.map { it.toMediaItem() }
            val tvShows = tvResponse.results.map { it.toMediaItem() }

            val allShows = (movies + tvShows).distinctBy { it.id }


            movieDao.insertAll(allShows.map { mediaItem ->
                MovieEntity(
                    id = mediaItem.id,
                    title = mediaItem.title,
                    author = mediaItem.author,
                    cover = mediaItem.cover,
                    publishYear = mediaItem.publishYear,
                    desc = mediaItem.desc,
                    genres = ""
                )
            })

            allShows
        }
    }

    override suspend fun getShowDetails(id: String): MediaItem {
        Log.d("Detail", "Spustilo sa načítanie detailu pre ID: $id")

        val local = movieDao.getById(id) // získa MovieDto s JOIN rating

        val oneDayMillis = 24 * 60 * 60 * 1000L
        val shouldFetchFromApi = networkStatusProvider.isOnline() && (System.currentTimeMillis() - local.updatedAt > oneDayMillis)

        if (shouldFetchFromApi) {
            val typePrefix = id.substringBefore("-")
            val tmdbId = id.substringAfter("-")

            if (typePrefix != "TV" && typePrefix != "MOV") {
                throw IllegalArgumentException("Invalid media ID format: $id")
            }

            val dto: tmdbDetailResponse = when (typePrefix) {
                "TV" -> tmdbApi.getTvShowDetails(tmdbId)
                "MOV" -> tmdbApi.getMovieDetails(tmdbId)
                else -> throw IllegalStateException("Unknown type prefix: $typePrefix")
            }

            val genres = dto.genres.joinToString(", ") { it.name }
            val author = dto.production_companies.joinToString(", ") { it.name }
            val publishYear = dto.release_date?.take(4)?.toIntOrNull() ?: 0
            val description = if (!dto.tagline.isNullOrBlank()) {
                "${dto.tagline}\n\n${dto.overview ?: ""}"
            } else {
                dto.overview ?: ""
            }

            val entity = MovieEntity(
                id = id,
                title = dto.title ?: "Unknown",
                author = author,
                cover = dto.poster_path?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
                publishYear = publishYear,
                desc = description,
                genres = genres,
                updatedAt = System.currentTimeMillis()
            )

            movieDao.insert(entity)
        }

        val updated = movieDao.getById(id)
        return updated.toMediaItem()
    }



}