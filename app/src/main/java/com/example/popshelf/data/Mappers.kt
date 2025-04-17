package com.example.popshelf.data

import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.GameEntity
import com.example.popshelf.domain.MediaItem

fun BookEntity.toMediaItem(): MediaItem = MediaItem(
    title = title,
    author = author,
    cover = cover,
    id = id,
    publishYear = publishYear,
    desc = desc ?: "Description not available"
)

fun Book.toMediaItem(): MediaItem = MediaItem(
    title = title ?: "Neznámy názov",
    author = author_name?.joinToString(", ") ?: "Neznámy autor",
    cover = cover_i?.toString() ?: "", // alebo nejaký placeholder obrázok
    id = key?.substringAfterLast('/') ?: "",
    publishYear = first_publish_year ?: 0,
    desc = "Description not available"
)

fun Game.toMediaItem(): MediaItem = MediaItem(
    title = name,
    author = (involved_companies?.map { it.toString() } ?: emptyList()).joinToString(", "),
    cover = cover?.toString() ?: "no-cover",
    id = id.toString(),
    publishYear = 1940,
    desc = summary ?: "No description available"
)

fun GameEntity.toMediaItem(): MediaItem = MediaItem(
    title = title,
    author = authors,
    cover = cover,
    id = id,
    publishYear = 1940,
    desc = summary
)

fun MediaItem.toGameEntity(): GameEntity = GameEntity(
    id = id,
    title = title,
    authors = author,
    cover = cover,
    released = 1940,
    summary = desc
)