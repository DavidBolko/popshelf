package com.example.popshelf.data

import com.example.popshelf.data.dto.BookDto
import com.example.popshelf.data.dto.GameDto
import com.example.popshelf.data.dto.MovieDto
import com.example.popshelf.data.dto.ShelfDto
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.GameEntity
import com.example.popshelf.data.local.entity.MovieEntity
import com.example.popshelf.data.remote.Book
import com.example.popshelf.data.remote.Game
import com.example.popshelf.data.remote.Movie
import com.example.popshelf.data.remote.TvShow
import com.example.popshelf.data.remote.tmdbDetailResponse
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

fun MovieEntity.toMediaItem(): MediaItem = MediaItem(
    title = title,
    author = author,
    cover = cover,
    id = id,
    publishYear = publishYear,
    desc = desc ?: "Description not available",
    mediaType = MediaType.MOVIES
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
    mediaType = MediaType.GAMES,
)



fun MediaItem.toGameEntity(): GameEntity = GameEntity(
    id = id,
    title = title,
    authors = author,
    cover = cover,
    released = 1940,
    summary = desc,
    updatedAt = 0L
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
        rating = rating ?: 0,
        comment = comment ?: "",
        shelfId = shelfId,
        status = status ?: ""
    )
}

fun Movie.toMediaItem(): MediaItem {
    return MediaItem(
        id = "MOV-${this.id}",
        title = this.title ?: "",
        author = "",
        cover = this.poster_path?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        publishYear = this.release_date?.take(4)?.toIntOrNull() ?: 0,
        desc = this.overview ?: "",
        mediaType = MediaType.MOVIES
    )
}

fun TvShow.toMediaItem(): MediaItem {
    return MediaItem(
        id = "TV-${this.id}",
        title = this.name ?: "",
        author = "",
        cover = this.poster_path?.let { "https://image.tmdb.org/t/p/w500$poster_path" } ?: "",
        publishYear = this.first_air_date?.take(4)?.toIntOrNull() ?: 0,
        desc = this.overview ?: "",
        mediaType = MediaType.MOVIES,
    )
}

fun MovieDto.toMediaItem(): MediaItem {
    return MediaItem(
        id = id,
        title = title,
        author = author,
        cover = cover,
        publishYear = publishYear,
        desc = desc ?: "No description available",
        mediaType = MediaType.MOVIES,
        rating = rating ?: 0,
        comment = comment ?: "",
        shelfId = shelfId,
        status = status ?: ""
    )
}

fun GameDto.toMediaItem(): MediaItem {
    return MediaItem(
        id = id,
        title = title,
        author = authors,
        cover = cover,
        publishYear = released,
        desc = summary,
        mediaType = MediaType.GAMES,
        rating = rating ?: 0,
        comment = comment ?: "",
        shelfId = shelfId,
        status = status ?: ""
    )
}


