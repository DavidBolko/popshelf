package com.example.popshelf.domain.useCases

import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.IBookRepository
import com.example.popshelf.domain.repository.IGameRepository
import com.example.popshelf.domain.repository.IMovieRepository
import com.example.popshelf.presentation.MediaType

/***
 * Special use case class, which request [MediaItem] from correct repository based on passed [MediaType]
 *  @property IBookRepository instance of BookRepository implementation.
 *  @property IGameRepository instance of GameRepository implementation.
 *  @property IMovieRepository instance of MovieRepository implementation.
 */
class GetMediaUseCase(private val IBookRepository: IBookRepository, private val IGameRepository: IGameRepository, private val IMovieRepository: IMovieRepository) {
    /**
     * It executes the use case
     * @return list of [MediaItem]s.
     */
    suspend fun execute(mediaType: MediaType, query: String, page: Int): List<MediaItem> {
        return when (mediaType) {
            MediaType.BOOKS -> IBookRepository.getBooksByQuery(query, page)
            MediaType.GAMES -> IGameRepository.getGamesByQuery(query, page)
            MediaType.MOVIES -> IMovieRepository.getMoviesByQuery(query, page)
        }
    }
}
