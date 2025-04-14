package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popshelf.data.local.entity.BookEntity

@Dao
interface BookDao {
    @Insert
    fun insert(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<BookEntity>)

    @Query("SELECT * FROM books WHERE LOWER(title) LIKE '%' || LOWER(:name) || '%'")
    fun findByName(name: String): List<BookEntity>

    @Query("SELECT * FROM books WHERE id = :id")
    fun findById(id: String): BookEntity
}