package com.example.popshelf.data

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Date

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

data class AccessTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("token_type") val tokenType: String
)

data class involvedCompaniesResponse(
    val company: Int,
    val developer: Boolean
)

data class companyResponse(
    val name: String
)


data class Game(
    val id: Long,
    val name: String,
    val summary: String?,
    val involved_companies: List<Int>?,
    val cover: Int?
)

data class GameSearchResponse(
    val results: List<Game> = emptyList()
)