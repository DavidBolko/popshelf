package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database representation of a book media item.
 *
 * @property id identifier of the book.
 * @property title title of the book.
 * @property author name of the book's author.
 * @property cover the book cover.
 * @property released release year of the book.
 * @property desc optional description or summary of the book.
 * @property updatedAt timestamp representing the last time the item was updated.
 */
@Entity(tableName = "Books")
data class BookEntity (
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val cover:String,
    val released: Int,
    val desc: String?,
    val updatedAt: Long = 0L
)