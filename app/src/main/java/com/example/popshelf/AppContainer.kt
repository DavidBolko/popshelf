package com.example.popshelf

import android.content.Context
import androidx.room.Room
import com.example.popshelf.data.NetworkMonitor
import com.example.popshelf.data.remote.bookApi
import com.example.popshelf.data.remote.gameApi
import com.example.popshelf.data.local.PopshelfDatabase
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.dao.GameDao
import com.example.popshelf.data.local.dao.MovieDao
import com.example.popshelf.data.local.dao.ShelfDao
import com.example.popshelf.data.local.dao.ShelfItemDao
import com.example.popshelf.data.remote.movieApi
import com.example.popshelf.data.repository.BookRepository
import com.example.popshelf.data.repository.GameRepository
import com.example.popshelf.data.repository.ShelfItemRepository
import com.example.popshelf.data.repository.ShelfRepository
import com.example.popshelf.data.repository.MovieRepository
import com.example.popshelf.domain.NetworkStatusProvider
import com.example.popshelf.domain.repository.IBookRepository
import com.example.popshelf.domain.repository.IGameRepository
import com.example.popshelf.domain.repository.IMovieRepository
import com.example.popshelf.domain.repository.IShelfItemRepositary
import com.example.popshelf.domain.repository.IShelfRepository
import com.example.popshelf.domain.useCases.GetMediaDetailUseCase
import com.example.popshelf.domain.useCases.GetMediaUseCase

/*** Container class which preserves everything the app needs. This class is used to create single instance
 * for each part of the application and pass them to classes where it's needed.
 * @property context local context of the application
 * @property networkMonitor instance of network monitor which observes and preserves the internet status of device.
 * @property shelfRepo instance of ShelfRepository implementation.
 * @property shelfItemRepo instance of ShelfItemRepository implementation.
 * @property getMediaDetailUseCase instance of GetMediaDetailUseCase use case class.
 * @property getMediaUseCase instance of GetMediaUseCase use case class.
 */
class AppContainer(private val context: Context) {
    private val db: PopshelfDatabase by lazy { Room.databaseBuilder(context, PopshelfDatabase::class.java, "popshelf_db").build() }

    private val bookDao: BookDao by lazy { db.bookDao() }

    private val gameDao: GameDao by lazy{ db.gameDao() }

    private val shelfDao: ShelfDao by lazy{ db.shelfDao() }
    private val movieDao: MovieDao by lazy{ db.movieDao() }
    private val shelfItemDao: ShelfItemDao by lazy {db.shelfItemDao()}

    private val monitor = NetworkMonitor(context)

    private val networkStatusProvider: NetworkStatusProvider = monitor
    val networkMonitor: NetworkMonitor = monitor

    private val bookRepo: IBookRepository by lazy { BookRepository(bookApi, bookDao, networkStatusProvider) }
    private val gameRepo: IGameRepository by lazy { GameRepository(gameApi, gameDao, networkStatusProvider) }
    private val movieRepo: IMovieRepository by lazy { MovieRepository(movieApi, movieDao, networkStatusProvider) }

    val getMediaDetailUseCase = GetMediaDetailUseCase(gameRepo, bookRepo, movieRepo)
    val getMediaUseCase = GetMediaUseCase(bookRepo, gameRepo, movieRepo)
    val shelfRepo: IShelfRepository by lazy { ShelfRepository(shelfDao) }
    val shelfItemRepo: IShelfItemRepositary by lazy { ShelfItemRepository(shelfItemDao, movieDao, shelfDao, bookDao, gameDao) }

}