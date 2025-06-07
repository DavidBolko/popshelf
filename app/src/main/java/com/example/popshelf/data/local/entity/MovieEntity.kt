package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database representation of a movie media item.
 *
 * @property id identifier of the movie.
 * @property title title of the movie.
 * @property author name of the movie's author, director, or creator.
 * @property cover the cover art of the movie.
 * @property released release year of the movie.
 * @property desc optional description or synopsis of the movie.
 * @property genres genres of the movie stored as a comma separated string.
 * @property updatedAt timestamp representing the last time the item was updated or cached.
 */
@Entity(tableName = "Movies")
data class MovieEntity (
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val cover:String,
    val released: Int,
    val desc: String?,
    val genres: String,
    val updatedAt: Long = 0L
)