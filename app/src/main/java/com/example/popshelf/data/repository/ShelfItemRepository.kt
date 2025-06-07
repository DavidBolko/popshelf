package com.example.popshelf.data.repository

import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.dao.GameDao
import com.example.popshelf.data.local.dao.MovieDao
import com.example.popshelf.data.local.dao.ShelfDao
import com.example.popshelf.data.local.dao.ShelfItemDao
import com.example.popshelf.data.local.entity.ShelfItemEntity
import com.example.popshelf.data.toMediaItem
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.IShelfItemRepositary

class ShelfItemRepository(private val shelfItemDao: ShelfItemDao, private val movieDao: MovieDao, private val shelfDao: ShelfDao, private val bookDao: BookDao, private val gameDao: GameDao): IShelfItemRepositary{
    override suspend fun addShelfItem(id: String, mediaType: String, status: String, rating: Int, comment: String, shelf: String) {
        val defaultShelf = shelfDao.getShelfByName(mediaType)
        val userShelfId = if (shelf != "None") shelfDao.getShelfByName(shelf).id else null

        val entity = ShelfItemEntity(
            itemId = id,
            mediaType = mediaType,
            status = status,
            rating = rating,
            comment = comment,
            defaultShelf = defaultShelf.id,
            shelfId = userShelfId
        )

        shelfItemDao.insertItem(entity)
    }

    override suspend fun getAllBooks(): List<MediaItem> {
        val items = shelfItemDao.getAllBooks()
        return items.map { it.toMediaItem() }
    }

    override suspend fun getShelfItems(id: Int?): List<MediaItem> {
        val items = when {
            id == null -> shelfItemDao.getShelfItems()
            id in 1..3 -> shelfItemDao.getItemsFromDefaultShelf(id)
            else -> shelfItemDao.getShelfItems(id)
        }
        val statusMap = items.associateBy { it.itemId }
        val books = items.filter { it.mediaType == "Books" }
        val games = items.filter { it.mediaType == "Games" }
        val movies = items.filter { it.mediaType == "Movies" }

        val bookEntities = bookDao.findById(books.map { it.itemId })
        val gameEntities = gameDao.findById(games.map { it.itemId })
        val movieEntities = movieDao.getById(movies.map { it.itemId })

        return buildList {
            addAll(bookEntities.map { it.toMediaItem(statusMap[it.id]?.status.orEmpty()) })
            addAll(gameEntities.map { it.toMediaItem(statusMap[it.id]?.status.orEmpty()) })
            addAll(movieEntities.map { it.toMediaItem(statusMap[it.id]?.status.orEmpty()) })
        }
    }

    override suspend fun deleteShelfItem(itemId: String) {
        this.shelfItemDao.deleteItem(itemId)
    }

    override suspend fun updateShelfItem(itemId: String, shelfId: Int?, status: String, rating: Int, comment: String) {
        val existing = shelfItemDao.getItemById(itemId)
        if (existing != null) {
            val updated = existing.copy(
                status = status,
                rating = rating,
                comment = comment,
                shelfId = shelfId
            )
            shelfItemDao.insertItem(updated)
        }
    }

}