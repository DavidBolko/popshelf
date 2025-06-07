package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.ShelfItemEntity

/**
 * Data access object for accessing and managing shelf items.
 *
 * Provides methods for inserting, updating, retrieving, and deleting media items
 * that are stored in user or system shelves.
 */
@Dao
interface ShelfItemDao {

    /**
     * Inserts or replaces a shelf item.
     * If the item already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShelfItemEntity)

    /**
     * Returns all books that are in ShelfItemEntity with mediaType = 'Books'.
     * @return [BookEntity]
     */
    @Query("SELECT * FROM Books " + "WHERE id IN (" + "SELECT itemId FROM ShelfItemEntity WHERE mediaType = 'Books'" + ")")
    suspend fun getAllBooks(): List<BookEntity>

    /**
     * Returns a system shelf record for the given item ID if it exists.
     */
    @Query("""SELECT * FROM ShelfItemEntity WHERE itemId = :id AND shelfId IN (SELECT id FROM ShelfEntity WHERE isSystem = 1) LIMIT 1""")
    suspend fun getFromDefaultById(id:String): ShelfItemEntity?

    /**
     * Returns all shelf items for a specific shelf.
     */
    @Query("SELECT * FROM ShelfItemEntity WHERE shelfId = :shelfId")
    suspend fun getShelfItems(shelfId: Int): List<ShelfItemEntity>

    /**
     * Returns all shelf items regardless of shelf.
     */
    @Query("SELECT * FROM ShelfItemEntity")
    suspend fun getShelfItems(): List<ShelfItemEntity>

    /**
     * Returns all items from a specific system shelf.
     */
    @Query("SELECT * FROM ShelfItemEntity WHERE defaultShelf = :shelfId")
    suspend fun getItemsFromDefaultShelf(shelfId: Int): List<ShelfItemEntity>

    /**
     * Updates the rating of a specific item.
     */
    @Query("UPDATE ShelfItemEntity SET rating = :rating WHERE itemId = :itemId")
    suspend fun updateRating(itemId: String, rating: Int)


    /**
     * Retrieves a shelf item by its item ID.
     * @return [ShelfItemEntity]
     */
    @Query("Select * from ShelfItemEntity where itemId = :itemId")
    suspend fun getItemById(itemId:String): ShelfItemEntity?

    /**
     * Updates the fields of an item including shelfId, status, rating, and comment.
     */
    @Query("""
        UPDATE ShelfItemEntity 
        SET status = :status, rating = :rating, comment = :comment, shelfId = :shelfId
        WHERE itemId = :itemId
    """)
    suspend fun updateItem(itemId: String, shelfId: Int, status: String, rating: Int, comment: String)

    /**
     * Deletes all instances of an item from all shelves.
     */
    @Query("delete from ShelfItemEntity where itemId = :itemId")
    suspend fun deleteItem(itemId: String)

    /**
     * Deletes the item only from user-defined shelves (excluding system shelves).
     * system shelves ids are 1, 2, and 3.
     */
    @Query("delete from ShelfItemEntity where itemId = :itemId and shelfId not in (1,2,3)")
    suspend fun deleteItemFromUserShelf(itemId: String)
}