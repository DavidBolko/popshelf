package com.example.popshelf.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.data.NetworkMonitor
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.R
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.IShelfItemRepositary
import com.example.popshelf.domain.useCases.GetMediaDetailUseCase
import com.example.popshelf.presentation.MediaType
import com.example.popshelf.presentation.UIEvent
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel class for preserving and requesting data for the Detail screen.
 *
 * @property data UI state containing detailed data of the selected media item.
 * @property state tells the UI it should navigate or some UI event error happened.
 * @property isConnected current network connection status observed via NetworkMonitor.
 * @property mediaType media type name passed via SavedStateHandle (e.g. Books, Movies).
 * @property fromShelf tells if the detail was opened from a shelf screen.
 * @constructor creates DetailViewModel with injected use case, repositories and saved state handle.
 *
 * @param getMediaDetailUseCase use case used to load detailed media item data from the correct source.
 * @param shelfItemRepository repository interface for accessing and deleting shelf items.
 * @param networkMonitor class used to observe network connection status of the device.
 * @param savedStateHandle used to retrieve navigation arguments such as item ID or media type.
 */
class DetailViewModel(private val getMediaDetailUseCase: GetMediaDetailUseCase, private val shelfItemRepository: IShelfItemRepositary, private val networkMonitor: NetworkMonitor, savedStateHandle: SavedStateHandle): ViewModel() {
    private val detail = MutableStateFlow<UIState<MediaItem>>(UIState.Loading)
    val data: StateFlow<UIState<MediaItem>> = detail

    private val _state = MutableSharedFlow<UIEvent>()
    val state: SharedFlow<UIEvent> = _state

    val isConnected = networkMonitor.isConnected

    private val id: String = savedStateHandle["id"] ?: ""
    val mediaType: String = savedStateHandle["mediaType"] ?: "Books"
    val fromShelf: Boolean = savedStateHandle["fromShelf"] ?: false

    init {
        loadData()
    }


    /**
     * Method runs the loadData method again if needed.
     */
    fun refresh(){
        loadData()
    }


    private fun loadData(){
        viewModelScope.launch {
            try {
                val item = getMediaDetailUseCase.execute(MediaType.valueOf(mediaType), id)
                detail.value = UIState.Success(item)
            } catch (e: Exception) {
                detail.value = UIState.Error(R.string.detail_error)
            }
        }
    }


    /**
     * Function which run after clicking the delete button inside the detail screen.
     */
    fun onDelete(){
        viewModelScope.launch {
            try{
                shelfItemRepository.deleteShelfItem(id)
                _state.emit(UIEvent.NavigateBack)
            } catch (e: Exception) {
                detail.value = UIState.Error(R.string.delete_error)
            }
        }
    }

    companion object {
        /**
         * Factory for creating [DetailViewModel] with dependencies from [PopshelfApplication].
         **/
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                DetailViewModel(app.getMediaDetailUseCase,  app.shelfItemRepo, app.networkMonitor,  savedStateHandle)
            }
        }
    }
}