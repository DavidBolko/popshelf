package com.example.popshelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popshelf.data.dto.MediaDto
import com.example.popshelf.data.local.entity.BookEntity

/**
 * Data access object for managing book records.
 *
 * Provides operations for inserting, searching, and retrieving books
 */
@Dao
interface BookDao {

    /**
     * Inserts a book into the database.
     * If a book with the same id already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookEntity)

    /**
     * Inserts a list of books into the database.
     * Entries that already exist will be ignored.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(books: List<BookEntity>)

    /**
     * Searches for books by partial title match.
     *
     * @param name search name.
     * @return List of [BookEntity]s.
     */
    @Query("SELECT * FROM books WHERE LOWER(title) LIKE '%' || LOWER(:name) || '%'")
    suspend fun findByName(name: String): List<BookEntity>

    /**
     * Retrieves detailed information about a book by its ID,
     * with shelf data like rating, comment, and status (if available).
     *
     * @param id identifier of the book.
     * @return [MediaDto] combining book data and shelf data.
     */
    @Query("""
    SELECT b.id, b.title, b.author, b.cover, b.released, b.`desc`, b.updatedAt, s.rating, s.comment, s.shelfId, s.status, "BOOKS" as mediaType
    FROM Books b
    LEFT JOIN ShelfItemEntity s ON b.id = s.itemId
    WHERE b.id = :id
""")
    suspend fun findById(id: String): MediaDto

    /**
     * Retrieves a list of books by their IDs.
     *
     * @param ids list of ids which should match with records
     * @return List [BookEntity]s.
     */
    @Query("SELECT * FROM books WHERE id IN (:ids)")
    suspend fun findById(ids: List<String>): List<BookEntity>

}