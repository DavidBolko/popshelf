package com.example.popshelf.data

import com.example.popshelf.data.dto.MediaDto
import com.example.popshelf.data.dto.ShelfDto
import com.example.popshelf.data.local.entity.BookEntity
import com.example.popshelf.data.local.entity.GameEntity
import com.example.popshelf.data.local.entity.MovieEntity
import com.example.popshelf.data.remote.Book
import com.example.popshelf.data.remote.Game
import com.example.popshelf.data.remote.Movie
import com.example.popshelf.data.remote.TvShow
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.Shelf
import com.example.popshelf.presentation.MediaType
import java.time.Instant
import java.time.ZoneId


//------------------------------------------------------------------------------//
//Mapuje sa z entit do MediaItem (entita je Room/Databazový typ)

/**
 * Maps a [BookEntity] to a [MediaItem].
 */
fun BookEntity.toMediaItem(): MediaItem = MediaItem(
    title = title,
    author = author,
    cover = cover,
    id = id,
    released = released,
    desc = desc ?: "Description not available",
    mediaType = MediaType.BOOKS
)
/**
 * Maps a [MovieEntity] to a [MediaItem].
 */
fun MovieEntity.toMediaItem(): MediaItem = MediaItem(
    title = title,
    author = author,
    cover = cover,
    id = id,
    released = released,
    desc = desc ?: "Description not available",
    mediaType = MediaType.MOVIES
)

/**
 * Maps a [GameEntity] to a [MediaItem] of type [MediaType.GAMES].
 */
fun GameEntity.toMediaItem(): MediaItem = MediaItem(
    title = title,
    author = author,
    cover = cover,
    id = id,
    released = 1940,
    desc = desc,
    mediaType = MediaType.GAMES,
)
//------------------------------------------------------------------------------//
//Mapuje sa z API typov do MediaItem

/**
 * Maps an external [Book] model to a [MediaItem].
 */
fun Book.toMediaItem(): MediaItem = MediaItem(
    title = title,
    author = author_name.joinToString(", "),
    cover = cover_i?.toString() ?: "",
    id = key.substringAfterLast('/'),
    released = first_publish_year,
    desc = "Description not available",
    mediaType = MediaType.BOOKS
)

/**
 * Maps an external [Game] model to a [MediaItem].
 */
fun Game.toMediaItem(): MediaItem = MediaItem(
    title = name,
    author = (involved_companies?.map { it.toString() } ?: emptyList()).joinToString(", "),
    cover = cover?.toString() ?: "no-cover",
    id = id.toString(),
    released = Instant.ofEpochSecond(first_release_date).atZone(ZoneId.systemDefault()).year,
    desc = summary ?: "No description available",
    mediaType = MediaType.GAMES
)
/**
 * Maps a [Movie] from external API to a [MediaItem].
 */
fun Movie.toMediaItem(): MediaItem {
    return MediaItem(
        id = "MOV-${this.id}",
        title = this.title ?: "",
        author = "",
        cover = this.poster_path?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        released = this.release_date?.take(4)?.toIntOrNull() ?: 0,
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
        released = this.first_air_date?.take(4)?.toIntOrNull() ?: 0,
        desc = this.overview ?: "",
        mediaType = MediaType.MOVIES,
    )
}
//-------------------------------------------------------------------------------------//


//------------------------------------------------------------------------------//
//Mapovanie z DTO objektov do iných. (DTO použité ako typy ktore už existuju len su rožšírene napr spojením dvoch tabuliek v Room)
/**
 * Converts a [ShelfDto] to a domain [Shelf] model.
 */
fun ShelfDto.toShelf(): Shelf = Shelf(
    id = id,
    image = image,
    name = name,
    color = color,
    itemCount = itemCount
)

/**
 * Converts a [MediaDto] to a [MediaItem].
 */
fun MediaDto.toMediaItem(): MediaItem {
    return MediaItem(
        id = id,
        title = title,
        author = author,
        cover = cover,
        released = released,
        desc = desc,
        mediaType = MediaType.valueOf(mediaType.uppercase()),
        rating = rating,
        comment = comment,
        shelfId = shelfId,
        status = status ?: "Planned"
    )
}

/**
 * Converts a [MediaItem] to a [GameEntity] for database storage.
 */
fun MediaItem.toGameEntity(): GameEntity = GameEntity(
    id = id,
    title = title,
    author = author,
    cover = cover,
    released = released,
    desc = desc,
)
