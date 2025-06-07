package com.example.popshelf.domain.repository

import com.example.popshelf.domain.MediaItem
import com.example.popshelf.presentation.MediaType

/** Repository interface for accessing [MediaItem] data of [MediaType] game.
 */
interface IGameRepository {
    /**
     * Fetches and stores details for specific book and returns it as [MediaItem].
     * @param id identifier of the book.
     * @return specific instance of [MediaItem] for book.
     */
    suspend fun getGamesByQuery(query: String, page: Int = 1): List<MediaItem>;

    /**
     * Fetches and stores details for specific book and returns it as [MediaItem].
     * @param id identifier of the book.
     * @return specific instance of [MediaItem] for book.
     */
    suspend fun getGameDetails(id: String): MediaItem
}