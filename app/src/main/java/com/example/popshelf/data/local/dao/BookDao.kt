package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.popshelf.data.local.entity.BookEntity

@Dao
interface BookDao {
    @Insert
    suspend fun insert(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(books: List<BookEntity>)

    @Query("SELECT * FROM books WHERE LOWER(title) LIKE '%' || LOWER(:name) || '%'")
    suspend fun findByName(name: String): List<BookEntity>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun findById(id: String): BookEntity

    @Query("UPDATE Books SET desc = :newDesc where id = :id")
    suspend fun updateDesc(id: String, newDesc: String): Void
}