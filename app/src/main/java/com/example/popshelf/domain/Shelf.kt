package com.example.popshelf.domain

/**
 * Represents a shelf within the application, typically used to organize media items.
 * @author David Bolko
 * @property id identifier for the shelf.
 * @property image image path representing the shelf's visual. (Nullable)
 * @property name display name of the shelf.
 * @property color a string representation of the shelf's color (String to hex codes defined in Colors.kt).
 * @property itemCount number of media items currently stored on the shelf.
 */
data class Shelf(
    val id: Int,
    val image: String?,
    val name: String,
    val color: String,
    val itemCount: Int
)