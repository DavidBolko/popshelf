package com.example.popshelf.presentation.viewmodels.factories

import com.example.popshelf.domain.useCases.GetBookUseCase
import com.example.popshelf.domain.useCases.GetGameUseCase
import com.example.popshelf.presentation.viewmodels.SearchViewModel

interface Factory<T> {
    fun create(): T
}

class SearchViewModelFactory(private val searchBookUseCase: GetBookUseCase, private val searchGameUseCase: GetGameUseCase): Factory<SearchViewModel>{
    override fun create(): SearchViewModel {
        return SearchViewModel(searchBookUseCase, searchGameUseCase)
    }
}