package com.example.popshelf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GamesCompanies")
data class GameCompanies (
    @PrimaryKey val id: String,
    val name: String,
)