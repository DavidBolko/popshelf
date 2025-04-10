package com.example.popshelf

import com.example.popshelf.data.bookApi
import com.example.popshelf.data.gameApi
import com.example.popshelf.data.repository.BookRepositoryImpl
import com.example.popshelf.data.repository.GameRepositoryImpl
import com.example.popshelf.domain.useCases.GetBookUseCase
import com.example.popshelf.domain.useCases.GetGameUseCase
import com.example.popshelf.presentation.viewmodels.factories.SearchViewModelFactory

class AppContainer {
    private val BookRepo = BookRepositoryImpl(bookApi)
    val searchBookUseCase = GetBookUseCase(BookRepo)

    private val GameRepo = GameRepositoryImpl(gameApi)
    val searchGameUseCase = GetGameUseCase(GameRepo)

    val searchViewModelFactory = SearchViewModelFactory(searchBookUseCase, searchGameUseCase)

}