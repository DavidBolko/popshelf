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
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.Shelf
import com.example.popshelf.domain.repository.ShelfItemRepositary
import com.example.popshelf.domain.repository.ShelfRepositary
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

/***
 * Viewmodel class for preserving and requesting data for AddEditScreen
 * @author David Bolko
 * @property shelfItemRepositary - repository for items of individual shelves.
 * @property  getMediaDetailUseCase - use case class, which contact correct repository based on selected media type.
 * @property  savedStateHandle - allows to access navigation arguments.
 * @property  networkMonitor - class which observe network status of the device.
 */
class AddEditItemViewModel(
    private val shelfItemRepositary: ShelfItemRepositary,
    private val shelfRepositary: ShelfRepositary,
    private val getMediaDetailUseCase: GetMediaDetailUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val id: String = savedStateHandle["id"] ?: ""
    private val mediaType: String = savedStateHandle["mediaType"] ?: "Books"
    private val shelfId: Int = savedStateHandle["shelfId"] ?: -1
    private val isEdit: Boolean = savedStateHandle["isEdit"] ?: false

    private val _state = MutableSharedFlow<UIEvent>()
    val state: SharedFlow<UIEvent> = _state

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

    lateinit var shelves: List<Shelf>

    val isConnected = networkMonitor.isConnected

    init {
        viewModelScope.launch {
            try {
                val item = getMediaDetailUseCase.execute(MediaType.valueOf(mediaType), id)

                shelves = shelfRepositary.getAllShelves(false)

                // Ak režim edit, načítaj existujúci záznam (vyplnený už existujúcimi udajmi)
                if (isEdit && shelfId != -1) {
                    val shelfItem = getMediaDetailUseCase.execute(MediaType.valueOf(mediaType), id)
                    _comment.value = shelfItem.comment ?: ""
                    _rating.value = shelfItem.rating
                    _selectedStatus.value = MediaStatus.valueOf(shelfItem.status.uppercase())
                    _selectedShelf.value = shelves.find { it.id == shelfId }?.name ?: "None"
                }
            _mediaItemState.value = UIState.Success(item)
            } catch (e: Exception) {
                _mediaItemState.value = UIState.Error("Chyba pri načítaní.")
            }
        }
    }

    /**
     * Function which run after user changed the status.
     */
    fun onStatusSelected(status: MediaStatus) {
        _selectedStatus.value = status
        if (status != MediaStatus.FINISHED) {
            _rating.value = 0
        }
    }

    /**
     * Function which run after user rewrote the comment.
     */
    fun onCommentChanged(comment: String) {
        _comment.value = comment
    }

    /**
     * Function which run after user changed the shelf.
     */
    fun onShelfSelected(shelf: String) {
        _selectedShelf.value = shelf
    }

    /**
     * Function which run after user changed the rating.
     */
    fun onRatingSelected(value: Int) {
        _rating.value = value
    }

    /**
     * Function which run after clicking the confirm button during editing or adding a work.
     */
    fun onConfirm() {
        val status = _selectedStatus.value
        val shelf = _selectedShelf.value
        val rating = _rating.value
        val comment = _comment.value

        viewModelScope.launch {
            try {
                val item = (mediaItemState.value as? UIState.Success)?.data
                if (item != null) {
                    if (isEdit) {
                        shelfItemRepositary.updateShelfItem(itemId = item.id, shelfId = shelves.find { it.name == shelf }?.id ?: shelfId, status = status.title, rating = rating, comment = comment)
                    } else {
                        shelfItemRepositary.addShelfItem(id = item.id, mediaType = MediaType.valueOf(mediaType).title, status = status.title, rating = rating, comment = comment.ifEmpty { "" }, shelf = shelf)
                    }
                }
                _state.emit(UIEvent.NavigateBack)
            } catch (e: Exception) {
                _state.emit(UIEvent.Error("Media couldn't be saved."))
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                AddEditItemViewModel(
                    app.shelfItemRepo,
                    app.shelfRepo,
                    app.getMediaDetailUseCase,
                    savedStateHandle,
                    app.networkMonitor
                )
            }
        }
    }
}
