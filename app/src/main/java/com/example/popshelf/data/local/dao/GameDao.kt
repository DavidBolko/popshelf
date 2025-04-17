package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.GameEntity

@Dao
interface GameDao {
    @Insert
    suspend fun insert(book: GameEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameEntity>)

    @Query("SELECT * FROM Games WHERE LOWER(title) LIKE '%' || LOWER(:name) || '%'")
    suspend fun findByName(name: String): List<GameEntity>

    @Query("SELECT * FROM Games WHERE id = :id")
    suspend fun findById(id: String): GameEntity

    @Query("UPDATE Games SET authors = :dev where id = :id")
    suspend fun updateDeveloper(id: String, dev: String): Void
}