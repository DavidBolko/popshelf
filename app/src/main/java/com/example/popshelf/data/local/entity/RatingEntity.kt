package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Rating")
data class RatingEntity (
    @PrimaryKey val itemId: String,
    val rating: Int = 0
)