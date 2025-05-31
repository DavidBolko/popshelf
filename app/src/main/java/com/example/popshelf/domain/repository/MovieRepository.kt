package com.example.popshelf.domain.repository

import com.example.popshelf.domain.MediaItem
import com.example.popshelf.presentation.MediaType

/** Repository interface for accessing [MediaItem] data of [MediaType] movie.
 *  @author David Bolko
 */
interface MovieRepository {
    suspend fun getMoviesByQuery(query: String, page: Int = 1): List<MediaItem>;
    suspend fun getShowDetails(id: String): MediaItem
}