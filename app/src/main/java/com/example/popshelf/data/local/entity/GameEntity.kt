package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Games")
data class GameEntity (
    @PrimaryKey val id: String,
    val title: String,
    val authors: String,
    val cover: String,
    val released: Int,
    val summary: String
)