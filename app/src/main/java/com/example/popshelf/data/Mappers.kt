package com.example.popshelf.data

import com.example.popshelf.data.dto.Book
import com.example.popshelf.data.dto.BookDto
import com.example.popshelf.data.dto.Game
import com.example.popshelf.data.dto.ShelfDto
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.GameEntity
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.Shelf
import com.example.popshelf.presentation.MediaType

fun BookEntity.toMediaItem(): MediaItem = MediaItem(
    title = title,
    author = author,
    cover = cover,
    id = id,
    publishYear = publishYear,
    desc = desc ?: "Description not available",
    mediaType = MediaType.BOOKS
)

fun Book.toMediaItem(): MediaItem = MediaItem(
    title = title ?: "Neznámy názov",
    author = author_name?.joinToString(", ") ?: "Neznámy autor",
    cover = cover_i?.toString() ?: "", // alebo nejaký placeholder obrázok
    id = key?.substringAfterLast('/') ?: "",
    publishYear = first_publish_year ?: 0,
    desc = "Description not available",
    mediaType = MediaType.BOOKS
)

fun Game.toMediaItem(): MediaItem = MediaItem(
    title = name,
    author = (involved_companies?.map { it.toString() } ?: emptyList()).joinToString(", "),
    cover = cover?.toString() ?: "no-cover",
    id = id.toString(),
    publishYear = 1940,
    desc = summary ?: "No description available",
    mediaType = MediaType.GAMES
)

fun GameEntity.toMediaItem(): MediaItem = MediaItem(
    title = title,
    author = authors,
    cover = cover,
    id = id,
    publishYear = 1940,
    desc = summary,
    mediaType = MediaType.GAMES
)



fun MediaItem.toGameEntity(): GameEntity = GameEntity(
    id = id,
    title = title,
    authors = author,
    cover = cover,
    released = 1940,
    summary = desc
)

fun ShelfDto.toShelf(): Shelf = Shelf(
    id = id,
    image = image,
    name = name,
    color = color,
    itemCount = itemCount
)

fun BookDto.toMediaItem(): MediaItem {
    return MediaItem(
        id = id,
        title = title,
        author = author,
        cover = cover,
        publishYear = publishYear,
        desc = desc ?: "No description available",
        mediaType = MediaType.BOOKS,
        rating = rating ?: 0
    )
}