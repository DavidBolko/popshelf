package com.example.popshelf

import android.content.Context
import androidx.room.Room
import com.example.popshelf.data.NetworkStatusProviderImpl
import com.example.popshelf.data.remote.bookApi
import com.example.popshelf.data.remote.gameApi
import com.example.popshelf.data.local.PopshelfDatabase
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.dao.GameDao
import com.example.popshelf.data.local.dao.MovieDao
import com.example.popshelf.data.local.dao.ShelfDao
import com.example.popshelf.data.local.dao.ShelfItemDao
import com.example.popshelf.data.remote.movieApi
import com.example.popshelf.data.repository.BookRepositoryImpl
import com.example.popshelf.data.repository.GameRepositoryImpl
import com.example.popshelf.data.repository.ShelfItemRepositoryImpl
import com.example.popshelf.data.repository.ShelfRepositoryImpl
import com.example.popshelf.data.repository.MovieRepositoryImpl
import com.example.popshelf.domain.repository.BookRepository
import com.example.popshelf.domain.repository.GameRepository
import com.example.popshelf.domain.repository.MovieRepository
import com.example.popshelf.domain.repository.ShelfItemRepositary
import com.example.popshelf.domain.repository.ShelfRepositary
import com.example.popshelf.domain.useCases.GetMediaDetailUseCase
import com.example.popshelf.domain.useCases.GetMediaUseCase

/*** Container class which preserves everything the app needs. This class is used to create single instance
 * for each part of the application and pass them to classes where it's needed.
 * @author David Bolko
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
    private val networkStatusProvider = NetworkStatusProviderImpl(context)
    private val shelfItemDao: ShelfItemDao by lazy {db.shelfItemDao()}

    private val bookRepo: BookRepository by lazy { BookRepositoryImpl(bookApi, bookDao, networkStatusProvider) }
    private val gameRepo: GameRepository by lazy { GameRepositoryImpl(gameApi, gameDao, networkStatusProvider) }
    private val movieRepo: MovieRepository by lazy { MovieRepositoryImpl(movieApi, movieDao, networkStatusProvider) }

    val networkMonitor: NetworkMonitor by lazy { NetworkMonitor(context)}
    val getMediaDetailUseCase = GetMediaDetailUseCase(gameRepo, bookRepo, movieRepo)
    val getMediaUseCase = GetMediaUseCase(bookRepo, gameRepo, movieRepo)
    val shelfRepo: ShelfRepositary by lazy { ShelfRepositoryImpl(shelfDao) }
    val shelfItemRepo: ShelfItemRepositary by lazy { ShelfItemRepositoryImpl(shelfItemDao, movieDao, shelfDao, bookDao, gameDao) }

}