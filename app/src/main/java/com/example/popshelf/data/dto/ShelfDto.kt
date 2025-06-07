package com.example.popshelf.data.dto


/**
 * Data transfer object representing a shelf with item count. Used in DAOs for joining two tables.
 *
 * @property id identifier of the shelf.
 * @property image optional image or icon of the shelf.
 * @property name name of the shelf.
 * @property color color representation for the shelf.
 * @property itemCount number of items stored in this shelf.
 */
data class ShelfDto(
    val id: Int,
    val image: String?,
    val name: String,
    val color: String,
    val itemCount: Int
)

