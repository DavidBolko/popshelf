package com.example.popshelf.domain.useCases

import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.IBookRepository
import com.example.popshelf.domain.repository.IGameRepository
import com.example.popshelf.domain.repository.IMovieRepository
import com.example.popshelf.presentation.MediaType

/***
 * Special use case class, which request details about [MediaItem] from correct repository based on passed [MediaType]
 *  @property IBookRepository instance of BookRepository implementation.
 *  @property IGameRepository instance of GameRepository implementation.
 *  @property IMovieRepository instance of MovieRepository implementation.
 */
class GetMediaDetailUseCase(private val IGameRepository: IGameRepository, private val IBookRepository: IBookRepository, private val IMovieRepository: IMovieRepository) {
    /**
     * It executes the use case
     * @return [MediaItem].
     *
     */
    suspend fun execute(mediaType: MediaType, id: String): MediaItem {
        return when (mediaType) {
            MediaType.BOOKS -> IBookRepository.getBookDetail(id)
            MediaType.GAMES -> IGameRepository.getGameDetails(id)
            MediaType.MOVIES -> IMovieRepository.getShowDetails(id)
        }
    }
}
