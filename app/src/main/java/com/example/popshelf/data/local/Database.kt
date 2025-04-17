package com.example.popshelf.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.dao.GameDao
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.GameEntity

@Database(entities = [BookEntity::class, GameEntity::class], version =1)
abstract class Database: RoomDatabase(){
    abstract fun bookDao(): BookDao
    abstract fun gameDao(): GameDao
}