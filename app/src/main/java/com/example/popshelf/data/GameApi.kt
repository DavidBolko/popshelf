package com.example.popshelf.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("https://api.rawg.io/api/").addConverterFactory(GsonConverterFactory.create()).build()
val gameApi = retrofit.create(GameApi::class.java)

interface GameApi {
    @GET("games")
    suspend fun findGames(@Query("search") query: String, @Query("search_exact") searchExact: Boolean = false, @Query("key")apiKey:String): GameSearchResponse
}

data class Game(
    val name: String,
    val background_image: String,
)

data class GameSearchResponse(
    val results: List<Game> = emptyList()
)