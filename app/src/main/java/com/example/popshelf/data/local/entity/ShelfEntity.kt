package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Database representation of a shelf.
 *
 * Each shelf can be created by the user or is defined by the system. Shelf names must be unique.
 * System shelves are typically generated automatically for each media type and cannot be deleted by the user.
 *
 * @property id identifier of the shelf.
 * @property name name of the shelf (must be unique).
 * @property color optional HEX color string used for displaying the shelf.
 * @property image optional image resource path or URI representing the shelf.
 * @property isSystem indicator that shelf is a system shelf or user created shelf.
 */
@Entity(tableName = "ShelfEntity", indices = [Index(value = ["name"], unique = true)])
data class ShelfEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: String = "",
    val image: String? = null,
    val isSystem: Boolean = false,
)