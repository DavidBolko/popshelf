package com.example.popshelf.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.entity.BookEntity

@Database(entities = [BookEntity::class], version =1)
abstract class Database: RoomDatabase(){
    abstract fun bookDao(): BookDao
}