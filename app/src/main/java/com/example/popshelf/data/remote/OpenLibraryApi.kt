package com.example.popshelf.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private val retrofit = Retrofit.Builder().baseUrl("https://openlibrary.org/").addConverterFactory(GsonConverterFactory.create()).build()
val bookApi = retrofit.create(BookApi::class.java)


/**
 * Retrofit interface for accessing book data from OpenLibrary API.
 * @author David Bolko
 */
interface BookApi {

    /**
     * Searches and retrieves books that matches query string criteria.
     *
     * @param title title to search for.
     * @param page page number for pagination.
     * @param limit maximum number of results per page.
     * @return A list of [Book]s.
     */
    @GET("search.json")
    suspend fun searchBooks(@Query("title") title: String, @Query("page") page: Int, @Query("limit") limit: Int): BookSearchResponse

    /**
     * Retrieves detailed information about a specific book.
     *
     * @param workId The unique identifier of the work.
     * @return [WorkDetailResponse] containing details.
     */
    @GET("works/{workId}.json")
    suspend fun getWorkDetail(@Path("workId") workId: String): WorkDetailResponse
}


//Typy, ktore sa vracajú z endpointov.

/**
 * Data class representing a book, every attribute is hardly defined by OpenLibraryAPI service.
 * @property title The title of the book.
 * @property author_name A list of author names associated with the book.
 * @property cover_i identifier for the cover image.
 * @property key work key used to retrieve detailed information about the work.
 * @property first_publish_year The year the book was first published.
 */
data class Book(
    val title: String,
    val author_name: List<String>,
    val cover_i: Int?,
    val key: String,
    val first_publish_year: Int
)


/**
 * Data class representing the detailed response for a specific work, every attribute is hardly defined by OpenLibraryAPI service.
 *
 * @property title The full title of the work.
 * @property description A description of the work
 * @property covers A list of identifiers of cover images.
 * @property subjects A list of categories associated with the work.
 */
data class WorkDetailResponse(
    val title: String?,
    val description: Any?,
    val covers: List<Int>?,
    val subjects: List<String>?
)

data class BookSearchResponse(
    val docs: List<Book>
)