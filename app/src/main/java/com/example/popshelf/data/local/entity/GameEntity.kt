package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database representation of a game media item.
 *
 * @property id identifier of the game.
 * @property title title of the game.
 * @property author name of the game's developer, studio, or creator.
 * @property cover the cover art of the game.
 * @property released release year of the game.
 * @property desc description or summary of the game.
 * @property updatedAt timestamp representing the last time the item was updated or cached.
 */
@Entity(tableName = "Games")
data class GameEntity (
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val cover: String,
    val released: Int,
    val desc: String,
    val updatedAt: Long = 0L
)