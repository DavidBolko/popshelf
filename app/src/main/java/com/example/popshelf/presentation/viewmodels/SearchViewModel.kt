package com.example.popshelf.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.NetworkMonitor
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

/***
 * Viewmodel class for preserving and requesting data for SearchViewModel
 * @author David Bolko
 * @property getMediaUseCase - use case class, which contact correct work repository based on selected media type.
 * @property networkMonitor - class which observe network status of the device.
 */
class SearchViewModel(private val getMediaUseCase: GetMediaUseCase, private val networkMonitor: NetworkMonitor) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<List<MediaItem>>>(UIState.Loading)
    val uiState: StateFlow<UIState<List<MediaItem>>> = _uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchType = MutableStateFlow(MediaType.BOOKS)
    val searchType: StateFlow<MediaType> = _searchType

    private var currentPage = 1
    private var isLoadingMore = false

    val isConnected = networkMonitor.isConnected

    fun updateSearchQuery(query: String, type: MediaType) {
        _searchQuery.value = query
        _searchType.value = type
        currentPage = 1
    }

    init {
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            combine(_searchQuery.debounce(500).distinctUntilChanged(), _searchType) { query, type ->
                query to type
            }.collect { (query, type) ->
                loadMedia(query, type, reset = true)
            }
        }
    }

    /**
     * Function which runs after scrolling search results and loads more.
     */
    fun loadMore() {
        if (isLoadingMore) return
        viewModelScope.launch {
            isLoadingMore = true
            try {
                currentPage++
                val query = _searchQuery.value
                val type = _searchType.value
                val newItems = getMediaUseCase.execute(type, query, currentPage)

                val currentItems = (_uiState.value as? UIState.Success)?.data ?: emptyList()
                _uiState.value = UIState.Success(currentItems + newItems)
            } catch (e: Exception) {
                _uiState.value = UIState.Error("Chyba pri načítaní ďalších výsledkov.")
            }
            isLoadingMore = false
        }
    }

    private suspend fun loadMedia(query: String, type: MediaType, reset: Boolean = false) {
        _uiState.value = UIState.Loading
        currentPage = 1
        try {
            val items = getMediaUseCase.execute(type, query, page = currentPage)
            _uiState.value = UIState.Success(items)
        } catch (e: Exception) {
            _uiState.value = UIState.Error("Chyba pri načítaní.")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                SearchViewModel(app.getMediaUseCase, app.networkMonitor)
            }
        }
    }
}