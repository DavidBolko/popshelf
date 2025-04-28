package com.example.popshelf.data.remote

import com.example.popshelf.data.dto.BookSearchResponse
import com.example.popshelf.data.dto.WorkDetailResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

