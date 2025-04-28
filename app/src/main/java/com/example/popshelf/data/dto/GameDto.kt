package com.example.popshelf.data.dto

import com.google.gson.annotations.SerializedName

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