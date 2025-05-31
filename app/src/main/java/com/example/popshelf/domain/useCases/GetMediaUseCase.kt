package com.example.popshelf.domain.useCases

import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.BookRepository
import com.example.popshelf.domain.repository.GameRepository
import com.example.popshelf.domain.repository.MovieRepository
import com.example.popshelf.presentation.MediaType

/***
 * Special use case class, which request [MediaItem] from correct repository based on passed [MediaType]
 * @author David Bolko
 *  @property bookRepository instance of BookRepository implementation.
 *  @property gameRepository instance of GameRepository implementation.
 *  @property movieRepository instance of MovieRepository implementation.
 */
class GetMediaUseCase(private val bookRepository: BookRepository, private val gameRepository: GameRepository, private val movieRepository: MovieRepository) {
    suspend fun execute(mediaType: MediaType, query: String, page: Int): List<MediaItem> {
        return when (mediaType) {
            MediaType.BOOKS -> bookRepository.getBooksByQuery(query, page)
            MediaType.GAMES -> gameRepository.getGamesByQuery(query, page)
            MediaType.MOVIES -> movieRepository.getMoviesByQuery(query, page)
        }
    }
}
