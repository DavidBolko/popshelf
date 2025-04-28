package com.example.popshelf.data.remote

import com.example.popshelf.data.dto.AccessTokenResponse
import com.example.popshelf.data.dto.Game
import com.example.popshelf.data.dto.companyResponse
import com.example.popshelf.data.dto.involvedCompaniesResponse
import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

private val authRetrofit = Retrofit.Builder().baseUrl("https://id.twitch.tv/").addConverterFactory(GsonConverterFactory.create()).build()
val authService = authRetrofit.create(GameApiAuth::class.java)

private val retrofit = Retrofit.Builder().baseUrl("https://api.igdb.com/v4/").addConverterFactory(GsonConverterFactory.create()).build()
val gameApi = retrofit.create(GameApi::class.java)

interface GameApi {
    @Headers("Content-Type: text/plain")
    @POST("games")
    suspend fun getGames(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authHeader: String,
        @Body body: RequestBody
    ): List<Game>

    @Headers("Content-Type: text/plain")
    @POST("involved_companies")
    suspend fun getInvolved(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authHeader: String,
        @Body body: RequestBody
    ): List<involvedCompaniesResponse>

    @Headers("Content-Type: text/plain")
    @POST("companies")
    suspend fun getCompany(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authHeader: String,
        @Body body: RequestBody
    ): List<companyResponse>
}

interface GameApiAuth {
    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "client_credentials"
    ): AccessTokenResponse
}

