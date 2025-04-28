package com.example.popshelf.data.repository

import android.provider.MediaStore.Audio.Media
import android.util.Log
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.dao.GameDao
import com.example.popshelf.data.local.dao.ShelfDao
import com.example.popshelf.data.local.dao.ShelfItemDao
import com.example.popshelf.data.local.entity.ShelfEntity
import com.example.popshelf.data.local.entity.ShelfItemEntity
import com.example.popshelf.data.toMediaItem
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.ShelfItemRepositary
import kotlin.math.log

class ShelfItemRepositoryImpl(private val shelfItemDao: ShelfItemDao, private val shelfDao: ShelfDao, private val bookDao: BookDao, private val gameDao: GameDao): ShelfItemRepositary{
    override suspend fun addShelfItem(id: String, mediaType: String, status: String, rating: Int, comment:String, shelf:String) {
        val defaultShelf = shelfDao.getShelfByName(mediaType)
        var shelfEntity: ShelfEntity
        if(shelf!="None"){
            shelfEntity = shelfDao.getShelfByName(shelf)
            shelfItemDao.insertItem(ShelfItemEntity(itemId = id, mediaType = mediaType,rating=rating, status = status, comment = comment, shelfId = shelfEntity.id))
        }
        val exists = shelfItemDao.getFromDefaultById(id)
        if(exists == null){
            shelfItemDao.insertItem(ShelfItemEntity(itemId = id, mediaType = mediaType,rating=rating, status = status, comment = comment, shelfId = defaultShelf.id))
        }

    }

    override suspend fun getAllBooks(): List<MediaItem> {
        val items = shelfItemDao.getAllBooks()
        return items.map { it.toMediaItem() }
    }

    override suspend fun getShelfItems(id: Int): List<MediaItem> {
        val items = shelfItemDao.getShelfItems(id)

        val books = items.filter { it.mediaType == "Books" }
        val games = items.filter { it.mediaType == "Games" }
        val bookEntities = bookDao.findById(books.map { it.itemId })
        val gameEntities = gameDao.findById(games.map { it.itemId })

        val mediaItems: List<MediaItem> = bookEntities.map { it.toMediaItem() } + gameEntities.map { it.toMediaItem() }
        return mediaItems
    }


}