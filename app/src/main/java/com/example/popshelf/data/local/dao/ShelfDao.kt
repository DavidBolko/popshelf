package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popshelf.data.dto.ShelfDto
import com.example.popshelf.data.local.entity.ShelfEntity

/**
 * Data access object for accessing and managing shelves.
 *
 * Provides methods for inserting, retrieving, and aggregating shelf data,
 * including both system and user shelves.
 */
@Dao
interface ShelfDao {

    /**
     * Returns all system shelves (where isSystem = true).
     */
    @Query("SELECT * FROM ShelfEntity WHERE isSystem = 1")
    suspend fun getAllDefaultShelves(): List<ShelfEntity>

    /**
     * Inserts multiple shelves into the database.
     * If a shelf already exists it will be ignored.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShelves(shelves: List<ShelfEntity>)

    /**
     * Inserts a single shelf into the database.
     * If a shelf with the same ID or name exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShelf(shelf: ShelfEntity)

    /**
     * Returns a list of shelves and item count of each shelf.
     *
     * If [isSystem] is false, returns only user shelves.
     * If [isSystem] is true, returns both system and user shelves.
     * @return [ShelfDto]
     */
    @Query("""
    SELECT 
        s.id, s.name, s.color, s.isSystem, s.image,
        COUNT(distinct i.itemId) AS itemCount
    FROM ShelfEntity s
    LEFT JOIN ShelfItemEntity i 
        ON (
            (s.isSystem = 1 AND i.defaultShelf = s.id) OR
            (s.isSystem = 0 AND i.shelfId = s.id)
        )
    WHERE (:isSystem = 1 OR s.isSystem = 0)
    GROUP BY s.id, s.name, s.color, s.isSystem, s.image
""")
    suspend fun getAllShelves(isSystem: Boolean = false): List<ShelfDto>

    /**
     * Returns a shelf by its unique name.
     * @return [ShelfEntity]
     */
    @Query("Select * from ShelfEntity where name = :name")
    suspend fun getShelfByName(name: String): ShelfEntity

    /**
     * deletes a shelf by it's id
     */
    @Query("Delete from ShelfEntity where id = :id")
    suspend fun delete(id: String)
}