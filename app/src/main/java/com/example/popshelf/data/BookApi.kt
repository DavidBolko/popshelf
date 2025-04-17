package com.example.popshelf.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private val retrofit = Retrofit.Builder().baseUrl("https://openlibrary.org/").addConverterFactory(GsonConverterFactory.create()).build()
val bookApi = retrofit.create(BookApi::class.java)

interface BookApi {
    @GET("search.json")
    suspend fun searchBooks(@Query("title") title: String): BookSearchResponse


    @GET("works/{workId}.json")
    suspend fun getWorkDetail(@Path("workId") workId: String): WorkDetailResponse
}


data class Book(
    val title: String,
    val author_name: List<String>,
    val cover_i: Int?,
    val key: String,
    val first_publish_year: Int
)

data class WorkDetailResponse(
    val title: String?,
    val description: Any?,
    val covers: List<Int>?,
    val subjects: List<String>?
)

data class BookSearchResponse(
    val docs: List<Book> = emptyList()
)