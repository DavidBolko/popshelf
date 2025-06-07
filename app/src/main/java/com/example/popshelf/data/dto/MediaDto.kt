package com.example.popshelf.data.dto

import com.example.popshelf.data.local.entity.ShelfItemEntity
/**
 * Data transfer object representing a media item  with user specific data.
 *
 * Combines media information (such as a book, movie, or game)
 * Returned by a JOIN between media tables and [ShelfItemEntity].
 *
 * @property id identifier of the media item.
 * @property title title of the media item.
 * @property author author of the item.
 * @property cover cover image.
 * @property released year when the item was released.
 * @property desc optional description of the item.
 * @property genres genres, stored as a comma-separated string.
 * @property updatedAt last update timestamp of the media item.
 * @property rating user rating (0 if unrated).
 * @property comment optional user comment about the item.
 * @property shelfId identifier of the user shelf this item belongs to (0 if not assigned).
 * @property status user selected status.
 * @property mediaType type of media, ("BOOKS", "MOVIES", or "GAMES").
 */
data class MediaDto(
    val id: String,
    val title: String,
    val author: String,
    val cover: String,
    val released: Int = 0,
    val desc: String = "",
    val genres: String? = "",
    val updatedAt: Long = 0L,
    val rating: Int = 0,
    val comment: String? = "",
    val shelfId: Int = 0,
    val status: String? = "",
    val mediaType: String
)
