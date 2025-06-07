package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popshelf.data.dto.MediaDto
import com.example.popshelf.data.local.entity.GameEntity

/**
 * Data access object for managing game records.
 *
 * Provides operations for inserting, searching, and retrieving games
 */
@Dao
interface GameDao {

    /**
     * Inserts a game into the database.
     * If a game with the same  already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: GameEntity)

    /**
     * Inserts a list of games into the database.
     * Entries that already exist will be ignored.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(games: List<GameEntity>)

    /**
     * Searches for games by partial title match (case-insensitive).
     *
     * @param name search name.
     * @return List [GameEntity]s.
     */
    @Query("SELECT * FROM Games WHERE LOWER(title) LIKE '%' || LOWER(:name) || '%'")
    suspend fun findByName(name: String): List<GameEntity>

    /**
     * Retrieves detailed information about a game by its ID,
     * with shelf data like rating, comment, and status (if available).
     *
     * @param id identifier of the game.
     * @return [MediaDto] combining game data and related shelf metadata.
     */
    @Query("""
    SELECT g.id, g.title, g.author, g.cover, g.released, g.`desc`,  g.updatedAt, s.rating, s.comment, s.shelfId, s.status, "GAMES" as mediaType
    FROM Games g
    LEFT JOIN ShelfItemEntity s ON g.id = s.itemId
    WHERE g.id = :id
""")
    suspend fun findById(id: String): MediaDto

    /**
     * Retrieves a list of games by their IDs.
     *
     * @param ids list of ids which should match with records.
     * @return List of [GameEntity]s
     */
    @Query("SELECT * FROM Games WHERE id IN (:ids)")
    suspend fun findById(ids: List<String>): List<GameEntity>

}