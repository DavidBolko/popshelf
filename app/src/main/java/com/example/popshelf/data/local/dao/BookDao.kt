package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popshelf.data.dto.MediaDto
import com.example.popshelf.data.local.entity.BookEntity

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(books: List<BookEntity>)

    @Query("SELECT * FROM books WHERE LOWER(title) LIKE '%' || LOWER(:name) || '%'")
    suspend fun findByName(name: String): List<BookEntity>

    @Query("""
    SELECT b.id, b.title, b.author, b.cover, b.released, b.`desc`, b.updatedAt, s.rating, s.comment, s.shelfId, s.status, "BOOKS" as mediaType
    FROM Books b
    LEFT JOIN ShelfItemEntity s ON b.id = s.itemId
    WHERE b.id = :id
""")
    suspend fun findById(id: String): MediaDto

    @Query("SELECT * FROM books WHERE id IN (:ids)")
    suspend fun findById(ids: List<String>): List<BookEntity>

}