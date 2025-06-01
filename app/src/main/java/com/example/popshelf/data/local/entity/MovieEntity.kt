package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class MovieEntity (
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val cover:String,
    val released: Int,
    val desc: String?,
    val genres: String,
    val updatedAt: Long = 0L
)