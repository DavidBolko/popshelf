package com.example.popshelf

import android.content.Context
import androidx.room.Room
import com.example.popshelf.data.bookApi
import com.example.popshelf.data.gameApi
import com.example.popshelf.data.local.Database
import com.example.popshelf.data.local.dao.BookDao
import com.example.popshelf.data.repository.BookRepositoryImpl
import com.example.popshelf.data.repository.GameRepositoryImpl
import com.example.popshelf.domain.repository.BookRepository
import com.example.popshelf.domain.useCases.GetBookUseCase
import com.example.popshelf.domain.useCases.GetGameUseCase
import com.example.popshelf.presentation.viewmodels.factories.SearchViewModelFactory

class AppContainer(private val context: Context) {
    val db: Database by lazy { Room.databaseBuilder(context, Database::class.java, "popshelf_db").build() }

    private val bookDao: BookDao by lazy {
        db.bookDao()
    }

    val BookRepo: BookRepository by lazy { BookRepositoryImpl(bookApi, bookDao) }
    val searchBookUseCase = GetBookUseCase(BookRepo)

    val GameRepo = GameRepositoryImpl(gameApi)
    val searchGameUseCase = GetGameUseCase(GameRepo)

    val searchViewModelFactory = SearchViewModelFactory(searchBookUseCase, searchGameUseCase)

}