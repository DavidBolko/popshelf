package com.example.popshelf.domain.useCases

import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.BookRepository
import com.example.popshelf.domain.repository.GameRepository
import com.example.popshelf.domain.repository.MovieRepository
import com.example.popshelf.presentation.MediaType

/***
 * Special use case class, which request details about [MediaItem] from correct repository based on passed [MediaType]
 * @author David Bolko
 *  @property bookRepository instance of BookRepository implementation.
 *  @property gameRepository instance of GameRepository implementation.
 *  @property movieRepository instance of MovieRepository implementation.
 */
class GetMediaDetailUseCase(private val gameRepository: GameRepository, private val bookRepository: BookRepository, private val movieRepository: MovieRepository) {
    suspend fun execute(mediaType: MediaType, id: String): MediaItem {
        return when (mediaType) {
            MediaType.BOOKS -> bookRepository.getBookDetail(id)
            MediaType.GAMES -> gameRepository.getGameDetails(id)
            MediaType.MOVIES -> movieRepository.getShowDetails(id)
        }
    }
}
