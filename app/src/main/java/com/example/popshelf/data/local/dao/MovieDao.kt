package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.popshelf.data.dto.MovieDto
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.GameEntity
import com.example.popshelf.data.local.entity.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM Movies WHERE LOWER(title) LIKE '%' || LOWER(:name) || '%'")
    suspend fun findByName(name: String): List<MovieEntity>

    @Query("""
    SELECT m.id, m.title, m.author, m.cover, m.publishYear, m.`desc`, m.genres, m.updatedAt, s.rating, s.comment, s.shelfId, s.status
    FROM Movies m
    LEFT JOIN ShelfItemEntity s ON m.id = s.itemId
    WHERE m.id = :id
""")
    suspend fun getById(id: String): MovieDto

    @Query("SELECT * FROM Movies WHERE id IN (:ids)")
    suspend fun getById(ids: List<String>): List<MovieEntity>

    /*
    @Query("SELECT * FROM Games WHERE id = :id")
    suspend fun findById(id: String): GameEntity

    @Query("SELECT * FROM Games WHERE id IN (:ids)")
    suspend fun findById(ids: List<String>): List<GameEntity>

    @Query("UPDATE Games SET authors = :dev where id = :id")
    suspend fun updateDeveloper(id: String, dev: String): Void
    */
}