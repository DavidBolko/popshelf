package com.example.popshelf.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.dao.GameDao
import com.example.popshelf.data.local.dao.MovieDao
import com.example.popshelf.data.local.dao.ShelfDao
import com.example.popshelf.data.local.dao.ShelfItemDao
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.GameEntity
import com.example.popshelf.data.local.entity.MovieEntity
import com.example.popshelf.data.local.entity.ShelfEntity
import com.example.popshelf.data.local.entity.ShelfItemEntity


/**
 * Abstract class of application database.
 * Contains constructors of DAOs for books, games, movies, shelves.
 *
 * @see BookDao
 * @see GameDao
 * @see ShelfDao
 * @see ShelfItemDao
 * @see MovieDao
 */
@Database(entities = [BookEntity::class, GameEntity::class, ShelfEntity::class, ShelfItemEntity::class, MovieEntity::class], version =1)
abstract class PopshelfDatabase: RoomDatabase(){
    abstract fun bookDao(): BookDao
    abstract fun gameDao(): GameDao
    abstract fun shelfDao(): ShelfDao
    abstract fun movieDao(): MovieDao
    abstract fun shelfItemDao(): ShelfItemDao
}