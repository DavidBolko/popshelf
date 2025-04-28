package com.example.popshelf.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.useCases.GetMediaUseCase
import com.example.popshelf.presentation.MediaType
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel(private val getMediaUseCase: GetMediaUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<List<MediaItem>>>(UIState.Loading)
    val uiState: StateFlow<UIState<List<MediaItem>>> = _uiState


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchType = MutableStateFlow<MediaType>(MediaType.BOOKS)
    val searchType: StateFlow<MediaType> = _searchType

    fun updateSearchQuery(query: String, type: MediaType) {
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
                loadMedia(query, type)
            }
        }
    }

    private suspend fun loadMedia(query: String, type: MediaType) {
        _uiState.value = UIState.Loading
        try {
            val allTasks = getMediaUseCase.execute(type, query)
            _uiState.value = UIState.Success(allTasks)
        } catch (e: Exception) {
            _uiState.value = UIState.Error("Chyba pri načítaní úloh.")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                SearchViewModel(app.getMediaUseCase)
            }
        }
    }
}