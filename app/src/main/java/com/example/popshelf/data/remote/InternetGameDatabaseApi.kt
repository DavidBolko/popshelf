package com.example.popshelf.data.remote

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

/**
 * Retrofit interface for accessing games data from IGDB API.
 */
interface GameApi {

    /**
     * Searches and retrieves games that matches query string criteria.
     *
     * @param clientId The IGDB client ID.
     * @param authHeader The authorization header. (form: "Bearer <token>").
     * @param body The plain text body containing the IGDB query.
     * @return A list of [Game]s.
     */
    @Headers("Content-Type: text/plain")
    @POST("games")
    suspend fun getGames(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authHeader: String,
        @Body body: RequestBody
    ): List<Game>

    /**
     * Searches and retrieves companies involved on developing specific game.
     *
     * @param clientId The IGDB client ID.
     * @param authHeader The authorization header. (form: "Bearer <token>").
     * @param body The plain text body containing the IGDB query.
     * @return A list of [Game]s.
     */
    @Headers("Content-Type: text/plain")
    @POST("involved_companies")
    suspend fun getInvolved(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authHeader: String,
        @Body body: RequestBody
    ): List<involvedCompaniesResponse>


    /**
     * Searches and retrieves details about developer companies involved on developing a specific game.
     *
     * @param clientId The IGDB client ID.
     * @param authHeader The authorization header. (form: "Bearer <token>").
     * @param body The plain text body containing the IGDB query.
     * @return A list of [companyResponse].
     */
    @Headers("Content-Type: text/plain")
    @POST("companies")
    suspend fun getCompany(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authHeader: String,
        @Body body: RequestBody
    ): List<companyResponse>
}

/**
 * Retrofit API for retrieving access tokens for IGDB API.
 */
interface GameApiAuth {

    /**
     * Requests an OAuth2 access token for IGDB API access.
     *
     * @param clientId The IGDB client ID.
     * @param clientSecret The IGDB client secret.
     * @param grantType Always "client_credentials".
     * @return An [AccessTokenResponse] object containing the token and expiration.
     */
    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "client_credentials"
    ): AccessTokenResponse
}

//Typy, ktore sa vracajú z endpointov.

/**
 * OAuth2 token response from Twitch authentication server.
 */
data class AccessTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("token_type") val tokenType: String
)

/**
 * Represents a response for an involved company on developing a game.
 *
 * @property company The ID of the company.
 * @property developer tells if company is a developer (or publisher).
 */
data class involvedCompaniesResponse(
    val company: Int,
    val developer: Boolean
)


/**
 * Represents basic information about a company.
 * @property name The name of the company.
 */
data class companyResponse(
    val name: String
)

/**
 * Data class representing a game, every attribute is hardly defined by IGDB API service.
 */
data class Game(
    val id: Long,
    val name: String,
    val summary: String?,
    val first_release_date: Long,
    val involved_companies: List<Int>?,
    val cover: Int?
)

