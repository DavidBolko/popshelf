package com.example.popshelf.data.repository

import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.dao.GameDao
import com.example.popshelf.data.local.dao.MovieDao
import com.example.popshelf.data.local.dao.ShelfDao
import com.example.popshelf.data.local.dao.ShelfItemDao
import com.example.popshelf.data.local.entity.ShelfItemEntity
import com.example.popshelf.data.toMediaItem
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.ShelfItemRepositary

class ShelfItemRepositoryImpl(private val shelfItemDao: ShelfItemDao, private val movieDao: MovieDao, private val shelfDao: ShelfDao, private val bookDao: BookDao, private val gameDao: GameDao): ShelfItemRepositary{
    override suspend fun addShelfItem(id: String, mediaType: String, status: String, rating: Int, comment: String, shelf: String) {
        val defaultShelf = shelfDao.getShelfByName(mediaType)
        shelfItemDao.updateRating(itemId = id, rating = rating)
        if (shelf != "None") {
            val shelfEntity = shelfDao.getShelfByName(shelf)

            shelfItemDao.insertItem(
                ShelfItemEntity(
                    itemId = id,
                    mediaType = mediaType,
                    rating = rating,
                    status = status,
                    comment = comment,
                    shelfId = shelfEntity.id
                )
            )
        }

        val exists = shelfItemDao.getFromDefaultById(id)
        if (exists == null) {
            shelfItemDao.insertItem(
                ShelfItemEntity(
                    itemId = id,
                    mediaType = mediaType,
                    rating = rating,
                    status = status,
                    comment = comment,
                    shelfId = defaultShelf.id
                )
            )
        }
    }

    override suspend fun getAllBooks(): List<MediaItem> {
        val items = shelfItemDao.getAllBooks()
        return items.map { it.toMediaItem() }
    }

    override suspend fun getShelfItems(id: Int): List<MediaItem> {
        val items = if (id in 1..3) {
            shelfItemDao.getItemsFromDefaultShelf(id)
        } else {
            shelfItemDao.getShelfItems(id)
        }

        val books = items.filter { it.mediaType == "Books" }
        val games = items.filter { it.mediaType == "Games" }
        val movies = items.filter { it.mediaType == "Movies" }

        val bookEntities = bookDao.findById(books.map { it.itemId })
        val gameEntities = gameDao.findById(games.map { it.itemId })
        val movieEntities = movieDao.getById(movies.map { it.itemId })

        return buildList {
            addAll(bookEntities.map { it.toMediaItem() })
            addAll(gameEntities.map { it.toMediaItem() })
            addAll(movieEntities.map { it.toMediaItem() })
        }
    }

    override suspend fun deleteShelfItem(itemId: String) {
        this.shelfItemDao.deleteItem(itemId)
    }


    override suspend fun updateShelfItem(itemId: String, shelfId: Int, status: String, rating: Int, comment: String) {
        shelfItemDao.updateItem(itemId, shelfId, status, rating, comment)
    }


}