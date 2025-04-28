package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.popshelf.data.dto.BookDto
import com.example.popshelf.data.local.entity.BookEntity

@Dao
interface BookDao {
    @Insert
    suspend fun insert(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(books: List<BookEntity>)

    @Query("SELECT * FROM books WHERE LOWER(title) LIKE '%' || LOWER(:name) || '%'")
    suspend fun findByName(name: String): List<BookEntity>

    @Query("""SELECT b.id, b.title, b.author, b.cover, b.publishYear, b.`desc`,s.rating FROM Books b LEFT JOIN ShelfItemEntity s ON b.id = s.itemId WHERE b.id = :id""")
    suspend fun findById(id: String): BookDto

    @Query("SELECT * FROM books WHERE id IN (:ids)")
    suspend fun findById(ids: List<String>): List<BookEntity>

    @Query("UPDATE Books SET desc = :newDesc where id = :id")
    suspend fun updateDesc(id: String, newDesc: String): Void
}