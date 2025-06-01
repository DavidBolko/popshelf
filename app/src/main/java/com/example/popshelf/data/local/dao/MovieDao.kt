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

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM Movies WHERE LOWER(title) LIKE '%' || LOWER(:name) || '%'")
    suspend fun findByName(name: String): List<MovieEntity>

    @Query("""
    SELECT m.id, m.title, m.author, m.cover, m.released, m.`desc`, m.genres,  m.updatedAt, s.rating, s.comment, s.shelfId, s.status, "MOVIES" as mediaType
    FROM Movies m
    LEFT JOIN ShelfItemEntity s ON m.id = s.itemId
    WHERE m.id = :id
""")
    suspend fun getById(id: String): MediaDto

    @Query("SELECT * FROM Movies WHERE id IN (:ids)")
    suspend fun getById(ids: List<String>): List<MovieEntity>

}