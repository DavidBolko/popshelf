package com.example.popshelf.data.repository

import com.example.popshelf.data.local.dao.ShelfDao
import com.example.popshelf.data.local.entity.ShelfEntity
import com.example.popshelf.data.toShelf
import com.example.popshelf.domain.Shelf
import com.example.popshelf.domain.repository.IShelfRepository


class ShelfRepository(private val shelfDao: ShelfDao): IShelfRepository{
    private val defaultShelves = listOf(
        ShelfEntity(name = "Books", image = "books", isSystem = true),
        ShelfEntity(name = "Movies",image = "movies", isSystem = true),
        ShelfEntity(name = "Games", image = "games", isSystem = true)
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

    override suspend fun deleteShelf(id: String) {
        shelfDao.delete(id)
    }


}