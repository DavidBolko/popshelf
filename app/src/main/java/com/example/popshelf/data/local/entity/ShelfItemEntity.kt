package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.popshelf.presentation.MediaType

/**
 * Database representation of added item by user.
 *
 * Each record holds the specific item and their relationship with system or user shelves.
 * If [shelfId] is null, the item is considered to belong only to the system shelf.
 *
 * The entity defines a foreign key relationship to [ShelfEntity], allowing cascade delete behavior.
 *
 * @property id identifier of the record.
 * @property itemId identifier of the saved item.
 * @property rating user rating for the media item.
 * @property comment optional user comment related to the item.
 * @property status current status of the item.
 * @property mediaType media type of the item represented by string name representaion of [MediaType].
 * @property shelfId identifier of the shelf.
 * @property defaultShelf identifier of system shelf this item belongs to (based on [mediaType]).
 */
@Entity(
    tableName = "ShelfItemEntity",
    foreignKeys = [
        ForeignKey(
            entity = ShelfEntity::class,
            parentColumns = ["id"],
            childColumns = ["shelfId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index(value = ["shelfId"])]
)
data class ShelfItemEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemId: String,
    val rating: Int,
    val comment: String? = "",
    val status: String,
    val mediaType: String,
    val shelfId: Int? = -1,
    val defaultShelf: Int = MediaType.valueOf(mediaType.uppercase()).number
)