package com.example.popshelf.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.R
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.repository.IShelfItemRepositary
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/***
 * Viewmodel class for preserving and requesting data for ShelfViewModel
 * @property getMediaUseCase - use case class, which contact correct work repository based on selected media type.
 * @param networkMonitor - class which observe network status of the device.
 */
class ShelfViewModel(private val IShelfItemRepositary: IShelfItemRepositary, savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _state = MutableStateFlow<UIState<List<MediaItem>>>(UIState.Loading)
    val state: StateFlow<UIState<List<MediaItem>>> = _state


    private val id: String = savedStateHandle["id"] ?: ""
    val name: String = savedStateHandle["name"] ?: "Shelf"

    init {

        viewModelScope.launch {
            try {
                _state.value = UIState.Success(IShelfItemRepositary.getShelfItems(id.toInt()))
            } catch (e: Exception) {
                _state.value = UIState.Error(R.string.unexpected_item_error)
            }
        }
    }



    companion object {
        /**
         * Factory for creating [ShelfViewModel] with dependencies from [PopshelfApplication].
         **/
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                ShelfViewModel(app.shelfItemRepo, savedStateHandle)
            }
        }
    }
}