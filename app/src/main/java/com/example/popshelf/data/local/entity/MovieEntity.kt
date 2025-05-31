package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Books")
data class BookEntity (
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val cover:String,
    val publishYear: Int,
    val desc: String?
)