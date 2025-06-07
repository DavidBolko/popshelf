package com.example.popshelf.data.repository

import com.Secrets
import com.example.popshelf.data.remote.GameApi
import com.example.popshelf.data.remote.authService
import com.example.popshelf.data.local.dao.GameDao
import com.example.popshelf.data.local.entity.GameEntity
import com.example.popshelf.data.toGameEntity
import com.example.popshelf.data.toMediaItem
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.NetworkStatusProvider
import com.example.popshelf.domain.repository.IGameRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class GameRepository(private val gameApi: GameApi, private val gameDao: GameDao, private val networkStatusProvider: NetworkStatusProvider): IGameRepository {
    override suspend fun getGamesByQuery(query: String, page: Int): List<MediaItem> {
        if (!networkStatusProvider.isOnline() || query.isEmpty()) {
            return gameDao.findByName(query).map { it.toMediaItem() }
        }

        val token = authService.getAccessToken(Secrets.idgb_id, Secrets.idgb_scrt).accessToken

        val apiGames = gameApi.getGames(
            clientId = Secrets.idgb_id,
            authHeader = "Bearer $token",
            body = """
            search "$query";
            fields id, name, involved_companies, summary, cover, first_release_date;
            limit 20;
            offset ${(page - 1) * 20};
        """.trimIndent().toRequestBody("text/plain".toMediaTypeOrNull())
        )

        val coverIds = apiGames.mapNotNull { it.cover }.distinct()

        val covers = if (coverIds.isNotEmpty()) {
            gameApi.getCovers(
                clientId = Secrets.idgb_id,
                authHeader = "Bearer $token",
                body = """
                fields image_id;
                limit 20;
                where id = (${coverIds.joinToString(",")});
            """.trimIndent().toRequestBody("text/plain".toMediaTypeOrNull())
            )
        } else emptyList()

        val coverMap = covers.associateBy { it.id }

        val mediaItems = apiGames.map { game ->
            val imageId = game.cover?.let { coverMap[it]?.imageId }
            game.toMediaItem(imageId)
        }

        gameDao.insertAll(mediaItems.map { it.toGameEntity() })

        return mediaItems
    }


    override suspend fun getGameDetails(id: String): MediaItem {
        var game = gameDao.findById(id)

        val dayMillis = 24 * 60 * 60 * 1000L
        val shouldFetch = networkStatusProvider.isOnline() && (System.currentTimeMillis() - game.updatedAt > dayMillis)

        if (shouldFetch) {
            val accessToken = authService.getAccessToken(Secrets.idgb_id, Secrets.idgb_scrt).accessToken
            val developer = getDeveloperNameFromInvolvedComp(game.author, accessToken)

            gameDao.insert(
                GameEntity(
                    id = game.id,
                    title = game.title,
                    author = developer,
                    cover = game.cover,
                    released = game.released,
                    desc = game.desc,
                    updatedAt = System.currentTimeMillis()
                )
            )

            game = gameDao.findById(id)
        }

        return game.toMediaItem()
    }


    private suspend fun getDeveloperNameFromInvolvedComp(involvedIds: String, token: String): String {
        val ids = involvedIds.removePrefix("[").removeSuffix("]").split(",").mapNotNull { it.trim().toIntOrNull() }

        if (ids.isEmpty()) return "No developer known."

        val involved = gameApi.getInvolved(
            clientId = Secrets.idgb_id,
            authHeader = "Bearer $token",
            body = """where id = (${ids.joinToString(",")}); fields company, developer;"""
                .trimIndent()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        )

        val developerIds = involved.filter { it.developer }.map { it.company }
        if (developerIds.isEmpty()) return "No developer known."

        val companies = gameApi.getCompany(
            clientId = Secrets.idgb_id,
            authHeader = "Bearer $token",
            body = """where id = (${developerIds.joinToString(",")}); fields name;"""
                .trimIndent()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        )

        return companies.joinToString(", ") { it.name }.ifBlank { "No developer known." }
    }
}