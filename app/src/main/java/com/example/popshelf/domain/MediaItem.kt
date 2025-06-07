package com.example.popshelf.domain

import com.example.popshelf.presentation.MediaType

/**
 * Represents a media item stored on a shelf, such as a book, movie or game work.
 *
 * @property title the title of the media item.
 * @property author the author or creator of the media item.
 * @property cover identifier of an image.
 * @property id a unique identifier for the media item.
 * @property released the year the item was published or released.
 * @property desc description or summary of the media item.
 * @property mediaType type of media defined by [MediaType].
 * @property rating the user selected rating (range 1-5). Default is 0.
 * @property comment user comment or personal note. Default is an empty string.
 * @property shelfId identifier of the shelf where this item is stored. Default is specific system shelf (1-3).
 * @property status string representing the current user status (example: "read", "watching", "planned").
 */
data class MediaItem(
    val title: String,
    val author: String,
    val cover:String,
    val id:String,
    val released: Int,
    val desc: String,
    val mediaType: MediaType,
    var rating: Int = 0,
    val comment: String? = "",
    val shelfId: Int? = 0,
    val status: String = ""
)