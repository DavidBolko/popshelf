package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.popshelf.presentation.MediaType

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
    val shelfId: Int,
    val defaultShelf: Int = MediaType.valueOf(mediaType.uppercase()).number
)