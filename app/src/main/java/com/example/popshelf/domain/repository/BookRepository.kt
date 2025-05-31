package com.example.popshelf.domain.repository

import com.example.popshelf.domain.MediaItem
import com.example.popshelf.presentation.MediaType


/** Repository interface for accessing [MediaItem] data of [MediaType] book
 *  @author David Bolko
 */
interface BookRepository {
    /**
     * Retrieves stored or search for books that match passed query string and number of a page.
     * @author David Bolko
     * @param query query string which is used to find exact books.
     * @param page page of query
     * @return list of [MediaItem]s which matches the search criteria (query).
     */
    suspend fun getBooksByQuery(query: String, page: Int = 1): List<MediaItem>;

    /**
     * Fetches and stores details for specific book and returns it as [MediaItem].
     * @author David Bolko
     * @param id identifier of the book.
     * @return specific instance of [MediaItem] for book.
     */
    suspend fun getBookDetail(id: String): MediaItem
}