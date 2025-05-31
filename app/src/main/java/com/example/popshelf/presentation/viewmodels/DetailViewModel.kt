package com.example.popshelf.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.NetworkMonitor
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.ShelfItemRepositary
import com.example.popshelf.domain.useCases.GetMediaDetailUseCase
import com.example.popshelf.presentation.MediaType
import com.example.popshelf.presentation.UIEvent
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/***
 * Viewmodel class for preserving and requesting data for DetailViewModel
 * @author David Bolko
 * @property shelfItemRepositary - repository for items of individual shelves.
 * @property getMediaDetailUseCase - use case class, which contact correct repository based on selected media type.
 * @property networkMonitor - class which observe network status of the device.
 * @param savedStateHandle - allows to access navigation arguments.
 */
class DetailViewModel(private val getMediaDetailUseCase: GetMediaDetailUseCase, private val shelfItemRepositary: ShelfItemRepositary, private val networkMonitor: NetworkMonitor, savedStateHandle: SavedStateHandle): ViewModel() {
    private val detail = MutableStateFlow<UIState<MediaItem>>(UIState.Loading)
    val data: StateFlow<UIState<MediaItem>> = detail

    private val _state = MutableSharedFlow<UIEvent>()
    val state: SharedFlow<UIEvent> = _state

    val isConnected = networkMonitor.isConnected

    private val id: String = savedStateHandle["id"] ?: ""
    val mediaType: String = savedStateHandle["mediaType"] ?: "Books"
    val fromShelf: Boolean = savedStateHandle["fromShelf"] ?: false
    init {
        viewModelScope.launch {
            try {
                Log.d("Error", mediaType)
                val item = getMediaDetailUseCase.execute(MediaType.valueOf(mediaType), id)
                detail.value = UIState.Success(item)
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
                detail.value = UIState.Error("Chyba pri načítaní.")
            }
        }
    }

    /**
     * Function which run after clicking the delete button inside the detail screen.
     */
    fun onDelete(){
        viewModelScope.launch {
            try{
                shelfItemRepositary.deleteShelfItem(id)
                _state.emit(UIEvent.NavigateBack)
            } catch (e: Exception) {
                detail.value = UIState.Error("Can't delete a record.")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                DetailViewModel(app.getMediaDetailUseCase,  app.shelfItemRepo, app.networkMonitor,  savedStateHandle)
            }
        }
    }
}