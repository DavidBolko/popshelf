package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ShelfEntity", indices = [Index(value = ["name"], unique = true)])
data class ShelfEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: String = "",
    val image: String? = null,
    val isSystem: Boolean = false,
)