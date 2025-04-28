package com.example.popshelf

import android.content.Context
import androidx.room.Room
import com.example.popshelf.data.remote.bookApi
import com.example.popshelf.data.remote.gameApi
import com.example.popshelf.data.local.Database
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.local.dao.GameDao
import com.example.popshelf.data.local.dao.ShelfDao
import com.example.popshelf.data.local.dao.ShelfItemDao
import com.example.popshelf.data.repository.BookRepositoryImpl
import com.example.popshelf.data.repository.GameRepositoryImpl
import com.example.popshelf.data.repository.ShelfItemRepositoryImpl
import com.example.popshelf.data.repository.ShelfRepositoryImpl
import com.example.popshelf.domain.repository.BookRepository
import com.example.popshelf.domain.repository.ShelfItemRepositary
import com.example.popshelf.domain.repository.ShelfRepositary
import com.example.popshelf.domain.useCases.AddItemUseCase
import com.example.popshelf.domain.useCases.GetMediaDetailUseCase
import com.example.popshelf.domain.useCases.GetMediaUseCase

class AppContainer(private val context: Context) {
    val db: Database by lazy { Room.databaseBuilder(context, Database::class.java, "popshelf_db").build() }

    private val bookDao: BookDao by lazy {
        db.bookDao()
    }

    private val gameDao: GameDao by lazy{
        db.gameDao()
    }

    private val shelfDao: ShelfDao by lazy{
        db.shelfDao()
    }

    private val shelfItemDao: ShelfItemDao by lazy {db.shelfItemDao()}

    val bookRepo: BookRepository by lazy { BookRepositoryImpl(bookApi, bookDao, shelfItemDao) }

    val gameRepo = GameRepositoryImpl(gameApi, gameDao)

    val getMediaDetailUseCase = GetMediaDetailUseCase(gameRepo, bookRepo)
    val getMediaUseCase = GetMediaUseCase(bookRepo, gameRepo)

    val ShelfRepo: ShelfRepositary by lazy { ShelfRepositoryImpl(shelfDao) }

    val shelfItemRepository: ShelfItemRepositary by lazy { ShelfItemRepositoryImpl(shelfItemDao, shelfDao, bookDao, gameDao) }
    val addItemUseCase = AddItemUseCase(shelfItemRepository)


}