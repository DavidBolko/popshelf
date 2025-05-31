package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popshelf.data.dto.ShelfDto
import com.example.popshelf.data.local.entity.ShelfEntity

@Dao
interface ShelfDao {
    @Query("SELECT * FROM ShelfEntity WHERE isSystem = 1")
    suspend fun getAllDefaultShelves(): List<ShelfEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShelves(shelves: List<ShelfEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShelf(shelf: ShelfEntity)

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

    @Query("Select * from ShelfEntity where name = :name")
    suspend fun getShelfByName(name: String): ShelfEntity
}