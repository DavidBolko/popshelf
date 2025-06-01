package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popshelf.data.dto.MediaDto
import com.example.popshelf.data.local.entity.GameEntity

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: GameEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(games: List<GameEntity>)

    @Query("SELECT * FROM Games WHERE LOWER(title) LIKE '%' || LOWER(:name) || '%'")
    suspend fun findByName(name: String): List<GameEntity>

    @Query("""
    SELECT g.id, g.title, g.author, g.cover, g.released, g.`desc`,  g.updatedAt, s.rating, s.comment, s.shelfId, s.status, "GAMES" as mediaType
    FROM Games g
    LEFT JOIN ShelfItemEntity s ON g.id = s.itemId
    WHERE g.id = :id
""")
    suspend fun findById(id: String): MediaDto

    @Query("SELECT * FROM Games WHERE id IN (:ids)")
    suspend fun findById(ids: List<String>): List<GameEntity>

}