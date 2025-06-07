package com.example.popshelf.domain.repository

import com.example.popshelf.domain.MediaItem
import com.example.popshelf.presentation.MediaType

/** Repository interface for accessing [MediaItem] data of [MediaType] movie.
 */
interface IMovieRepository {

    /**
     * Searches for movies matching the query.
     *
     * @param query search query.
     * @param page optional page number for paginated results (default is 1).
     * @return A list of [MediaItem]s.
     */
    suspend fun getMoviesByQuery(query: String, page: Int = 1): List<MediaItem>

    /**
     * Retrieves detailed information about a movie by its id.
     *
     * @param id identifier of the movie.
     * @return A [MediaItem] representation of the movie.
     */
    suspend fun getShowDetails(id: String): MediaItem
}