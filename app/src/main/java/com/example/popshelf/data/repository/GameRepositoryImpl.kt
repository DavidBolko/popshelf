package com.example.popshelf.data.repository

import com.Secrets
import com.example.popshelf.data.GameApi
import com.example.popshelf.data.authService
import com.example.popshelf.data.local.dao.GameDao
import com.example.popshelf.data.toGameEntity
import com.example.popshelf.data.toMediaItem
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.GameRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class GameRepositoryImpl(private val gameApi: GameApi, private val gameDao: GameDao): GameRepository {
    override suspend fun findGamesByTitle(title: String): List<MediaItem> {
        val games = gameDao.findByName(title)

        return if (games.isNotEmpty()) {
            games.map { it.toMediaItem() }
        } else {
            val token = authService.getAccessToken(Secrets.idgb_id, Secrets.idgb_scrt).accessToken
            val apiGames = gameApi.getGames(
                clientId = Secrets.idgb_id,
                authHeader = "Bearer $token",
                body = """search "$title";fields id, name, involved_companies, summary, cover;"""
                    .trimIndent()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
            )

            val mediaItems = apiGames.map { it.toMediaItem() }
            gameDao.insertAll(mediaItems.map { it.toGameEntity() })
            mediaItems
        }
    }

    override suspend fun getGameDetails(id: String): MediaItem {
        var game = gameDao.findById(id)
        val accessToken = authService.getAccessToken(Secrets.idgb_id, Secrets.idgb_scrt).accessToken

        val developer = getDeveloperNameFromInvolvedComp(game.authors, accessToken)

        gameDao.updateDeveloper(id, developer)
        game = gameDao.findById(id)
        return game.toMediaItem()
    }

    private suspend fun getDeveloperNameFromInvolvedComp(involvedIds: String, token: String): String {
        val ids = involvedIds
            .removePrefix("[")
            .removeSuffix("]")
            .split(",")
            .mapNotNull { it.trim().toIntOrNull() }

        if (ids.isEmpty()) return "No developer known."

        val involved = gameApi.getInvolved(
            clientId = Secrets.idgb_id,
            authHeader = "Bearer $token",
            body = """where id = (${ids.joinToString(",")}); fields company, developer;"""
                .trimIndent()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        )

        val developerIds = involved.filter { it.developer }.mapNotNull { it.company }
        if (developerIds.isEmpty()) return "No developer known."

        val companies = gameApi.getCompany(
            clientId = Secrets.idgb_id,
            authHeader = "Bearer $token",
            body = """where id = (${developerIds.joinToString(",")}); fields name;"""
                .trimIndent()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        )

        return companies.mapNotNull { it.name }.joinToString(", ").ifBlank { "No developer known." }
    }
}