package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.ShelfItemEntity

@Dao
interface ShelfItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShelfItemEntity)

    @Query("SELECT * FROM Books " + "WHERE id IN (" + "SELECT itemId FROM ShelfItemEntity WHERE mediaType = 'Books'" + ")")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("""SELECT * FROM ShelfItemEntity WHERE itemId = :id AND shelfId IN (SELECT id FROM ShelfEntity WHERE isSystem = 1) LIMIT 1""")
    suspend fun getFromDefaultById(id:String): ShelfItemEntity?

    @Query("SELECT * FROM ShelfItemEntity WHERE shelfId = :shelfId")
    suspend fun getShelfItems(shelfId: Int): List<ShelfItemEntity>

    @Query("SELECT * FROM ShelfItemEntity")
    suspend fun getShelfItems(): List<ShelfItemEntity>

    @Query("SELECT * FROM ShelfItemEntity WHERE defaultShelf = :shelfId")
    suspend fun getItemsFromDefaultShelf(shelfId: Int): List<ShelfItemEntity>

    @Query("UPDATE ShelfItemEntity SET rating = :rating WHERE itemId = :itemId")
    suspend fun updateRating(itemId: String, rating: Int)

    @Query("Select * from ShelfItemEntity where id = :id")
    suspend fun getItemById(id:String): ShelfItemEntity?


    @Query("""
        UPDATE ShelfItemEntity 
        SET status = :status, rating = :rating, comment = :comment, shelfId = :shelfId
        WHERE itemId = :itemId
    """)
    suspend fun updateItem(itemId: String, shelfId: Int, status: String, rating: Int, comment: String)

    @Query("delete from ShelfItemEntity where itemId = :itemId")
    suspend fun deleteItem(itemId: String)
}