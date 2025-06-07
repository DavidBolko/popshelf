package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.popshelf.data.dto.MediaDto
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.GameEntity
import com.example.popshelf.data.local.entity.MovieEntity

/**
 * Data access object for managing movie records.
 *
 * Provides operations for inserting, searching, and retrieving movies
 */
@Dao
interface MovieDao {

    /**
     * Inserts a movie into the database.
     * If a movie with the same id already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    /**
     * Inserts multiple movies into the database.
     * Existing entries with the same id will be ignored.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(movies: List<MovieEntity>)

    /**
     * Searches for movies by title match
     *
     * @param name searching name.
     * @return List of [MovieEntity]s
     */
    @Query("SELECT * FROM Movies WHERE LOWER(title) LIKE '%' || LOWER(:name) || '%'")
    suspend fun findByName(name: String): List<MovieEntity>

    /**
     * Retrieves detailed information about a movie by ID, with user data if available.
     *
     * @param id identifier of the movie to retrieve.
     * @return [MediaDto] containing combined movie and shelf item data.
     */
    @Query("""
    SELECT m.id, m.title, m.author, m.cover, m.released, m.`desc`, m.genres,  m.updatedAt, s.rating, s.comment, s.shelfId, s.status, "MOVIES" as mediaType
    FROM Movies m
    LEFT JOIN ShelfItemEntity s ON m.id = s.itemId
    WHERE m.id = :id
""")
    suspend fun getById(id: String): MediaDto

    /**
     * Retrieves a list of movies by their IDs.
     *
     * @param ids list of ids which should match with records.
     * @return List of [MovieEntity]s.
     */
    @Query("SELECT * FROM Movies WHERE id IN (:ids)")
    suspend fun getById(ids: List<String>): List<MovieEntity>

}