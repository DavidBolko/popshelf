package com.example.popshelf.data.repository

import com.example.popshelf.data.local.dao.ShelfDao
import com.example.popshelf.data.local.entity.ShelfEntity
import com.example.popshelf.data.toShelf
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.Shelf
import com.example.popshelf.domain.repository.ShelfRepositary


class ShelfRepositoryImpl(private val shelfDao: ShelfDao): ShelfRepositary{
    val defaultShelves = listOf(
        ShelfEntity(name = "Books", image = "books", isSystem = true),
        ShelfEntity(name = "Movies",image = "games", isSystem = true),
        ShelfEntity(name = "Games", image = "movies", isSystem = true)
    )

    override suspend fun createShelf(name: String, color:String) {
        shelfDao.insertShelf(ShelfEntity(name = name, color=color))
    }

    override suspend fun getAllShelves(isSystem:Boolean): List<Shelf> {
        val shelves = shelfDao.getAllShelves(isSystem)
        return shelves.map { it.toShelf() }
    }

    override suspend fun createDefaultShelves() {
        val existing = shelfDao.getAllDefaultShelves()
        if (existing.isEmpty()) {
            shelfDao.insertShelves(defaultShelves)
        }
    }

}