package com.example.popshelf.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.popshelf.data.BookApi
import com.example.popshelf.data.bookApi
import com.example.popshelf.data.gameApi
import com.example.popshelf.data.repository.BookRepositoryImpl
import com.example.popshelf.domain.useCases.GetBookUseCase
import com.example.popshelf.domain.useCases.GetGameUseCase
import com.example.popshelf.presentation.UIState
import com.example.popshelf.presentation.components.GameItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel(private val searchBookUseCase: GetBookUseCase, private val searchGameUseCase: GetGameUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<List<Any>>>(UIState.Loading)
    val uiState: StateFlow<UIState<List<Any>>> = _uiState
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    private val _searchType = MutableStateFlow(0)

    fun updateSearchQuery(query: String, type: Int) {
        _searchQuery.value = query
        _searchType.value = type
    }

    init {
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            combine(_searchQuery.debounce(500).distinctUntilChanged(), _searchType) { query, type ->
                query to type
            }.collect { (query, type) ->
                when(type) {
                    0 -> loadBook(query)
                    1 -> loadGame(query)
                }
            }
        }
    }

    private suspend fun loadBook(query: String) {
        _uiState.value = UIState.Loading
        try {
            val allTasks = searchBookUseCase.execute(query)
            _uiState.value = UIState.Success(allTasks)
        } catch (e: Exception) {
            _uiState.value = UIState.Error("Chyba pri načítaní úloh.")
        }
    }

    private suspend fun loadGame(query: String) {
        _uiState.value = UIState.Loading
        try {
            val foundGames = searchGameUseCase.execute(query)
            _uiState.value = UIState.Success(foundGames)
        } catch (e: Exception) {
            _uiState.value = UIState.Error("Chyba pri načítaní úloh.")
        }
    }
}