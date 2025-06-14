package com.example.popshelf.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.data.NetworkMonitor
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.R
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

/**
 * ViewModel class for preserving and requesting data for the search screen.
 *
 * @property uiState UI state representing the current list of media items or loading/error state.
 * @property searchQuery current search query typed by the user.
 * @property searchType currently selected media type (Books, Movies..).
 * @property isConnected current network connection status from NetworkMonitor.
 * @constructor creates SearchViewModel with injected use case and network monitor.
 *
 * @param getMediaUseCase use case class responsible for providing media items based on selected media type and query.
 * @param networkMonitor class used to observe network connection status of the device.
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


    /**
     * This functions changes values of MutableStateFlows defined in this viewmodel, after values are changed in UI to reflect the change.
     */
    fun updateSearchQuery(query: String, type: MediaType) {
        _searchQuery.value = query
        _searchType.value = type
        currentPage = 1
    }

    init {
        observeSearchQuery()
    }

    fun refresh(){
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
                _uiState.value = UIState.Error(R.string.search_more_error)
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
            _uiState.value = UIState.Error(R.string.search_error)
        }
    }

    companion object {
        /**
         * Factory for creating [SearchViewModel] with dependencies from [PopshelfApplication].
         **/
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                SearchViewModel(app.getMediaUseCase, app.networkMonitor)
            }
        }
    }
}