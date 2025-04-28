package com.example.popshelf.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.Shelf
import com.example.popshelf.domain.repository.ShelfRepositary
import com.example.popshelf.domain.useCases.AddItemUseCase
import com.example.popshelf.domain.useCases.GetMediaDetailUseCase
import com.example.popshelf.presentation.MediaStatus
import com.example.popshelf.presentation.MediaType
import com.example.popshelf.presentation.UIEvent
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddItemViewModel(private val addItemUseCase: AddItemUseCase, private val shelfRepositary: ShelfRepositary, getMediaDetailUseCase: GetMediaDetailUseCase, private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _state = MutableSharedFlow<UIEvent>()
    val state: SharedFlow<UIEvent> = _state

    private val _shelfState = MutableStateFlow<UIState<List<Shelf>>>(UIState.Loading)
    val shelfState: StateFlow<UIState<List<Shelf>>> = _shelfState

    private val _mediaItemState = MutableStateFlow<UIState<MediaItem>>(UIState.Loading)
    val mediaItemState: StateFlow<UIState<MediaItem>> = _mediaItemState

    private val _selectedStatus = MutableStateFlow(MediaStatus.FINISHED)
    val selectedStatus: StateFlow<MediaStatus> = _selectedStatus

    private val _comment = MutableStateFlow("")
    val comment: StateFlow<String> = _comment

    private val _selectedShelf = MutableStateFlow("None")
    val selectedShelf: StateFlow<String> = _selectedShelf

    private val _rating = MutableStateFlow(0)
    val rating: StateFlow<Int> = _rating

    private val id: String = savedStateHandle["id"] ?: ""
    val mediaType: String = savedStateHandle["mediaType"] ?: "Books"

    init {
        viewModelScope.launch {
            try {
                val item = getMediaDetailUseCase.execute(MediaType.valueOf(mediaType), id);
                val shelves = shelfRepositary.getAllShelves(false)
                _shelfState.value = UIState.Success(shelves)
                _mediaItemState.value = UIState.Success(item)
            } catch (e: Exception) {
                _mediaItemState.value = UIState.Error("Chyba pri načítaní.")
            }
        }
    }

    fun onStatusSelected(status: MediaStatus) {
        _selectedStatus.value = status
        if (status != MediaStatus.FINISHED) {
            _rating.value = 0
        }
    }
    fun onCommentChanged(comment:String) {
        _comment.value = comment
    }
    fun onShelfSelected(shelf: String) {
        _selectedShelf.value = shelf
    }

    fun onRatingSelected(value: Int) {
        _rating.value = value
    }

    fun onConfirm() {
        val status = _selectedStatus.value
        val shelf = _selectedShelf.value
        val rating = _rating.value

        viewModelScope.launch {
            try {
                val item = (mediaItemState.value as? UIState.Success)?.data
                if (item != null) {
                    addItemUseCase.execute(item.id, MediaType.valueOf(mediaType).title, status.title, rating, "", shelf)
                }
                _state.emit(UIEvent.NavigateBack)
            } catch (e: Exception) {
                _state.emit(UIEvent.Error("Media couldn't be added."))
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                AddItemViewModel(app.addItemUseCase, app.ShelfRepo, app.getMediaDetailUseCase, savedStateHandle)
            }
        }
    }
}