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
import com.example.popshelf.domain.Shelf
import com.example.popshelf.domain.repository.IShelfItemRepositary
import com.example.popshelf.domain.repository.IShelfRepository
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
 * @property IShelfItemRepositary - repository for items of individual shelves.
 * @property  getMediaDetailUseCase - use case class, which contact correct repository based on selected media type.
 * @property  savedStateHandle - allows to access navigation arguments.
 * @property  networkMonitor - class which observe network status of the device.
 */
class AddEditItemViewModel(
    private val IShelfItemRepositary: IShelfItemRepositary,
    private val IShelfRepository: IShelfRepository,
    private val getMediaDetailUseCase: GetMediaDetailUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    //Atributy ktoré sme dostali z navigačnych parametrov
    private val idParam: String = savedStateHandle["id"] ?: ""
    private val mediaTypeParam: String = savedStateHandle["mediaType"] ?: "Books"
    private val shelfIdParam: Int? = savedStateHandle["shelfId"]
    private val isEditParam: Boolean = savedStateHandle["isEdit"] ?: false

    //Stav UI po vykonani Interakcie s tlačidlom
    private val _state = MutableSharedFlow<UIEvent>()
    val state: SharedFlow<UIEvent> = _state

    //Stav načitavaneho itemu na zobrazenie
    private val _mediaItemState = MutableStateFlow<UIState<MediaItem>>(UIState.Loading)
    val mediaItemState: StateFlow<UIState<MediaItem>> = _mediaItemState

    //Použivatelom menene UI stavy
    private val _mediaStatus = MutableStateFlow(MediaStatus.FINISHED)
    val mediaStatus: StateFlow<MediaStatus> = _mediaStatus

    private val _comment = MutableStateFlow("")
    val comment: StateFlow<String> = _comment

    private val _shelf = MutableStateFlow("None")
    val shelf: StateFlow<String> = _shelf

    private val _rating = MutableStateFlow(0)
    val rating: StateFlow<Int> = _rating

    //List poličiek
    private var shelves: List<Shelf> = emptyList()

    val isConnected = networkMonitor.isConnected

    //Viewmodel sa incializuje načitanim detailu itemu, ziskaju sa poličky ak sme v rezime edit,
    //vyplnia sa kolonky načitanim udajov z databazy (ako už uživatel hodnotil a podobne.)
    init {
        viewModelScope.launch {
            try {
                val item = getMediaDetailUseCase.execute(MediaType.valueOf(mediaTypeParam), idParam)

                shelves = IShelfRepository.getAllShelves(false)

                // Ak režim edit, načítaj existujúci záznam (vyplnený už existujúcimi udajmi)
                if (isEditParam && shelfIdParam != null) {
                    val shelfItem = getMediaDetailUseCase.execute(MediaType.valueOf(mediaTypeParam), idParam)
                    _comment.value = shelfItem.comment ?: ""
                    _rating.value = shelfItem.rating
                    _mediaStatus.value = MediaStatus.valueOf(shelfItem.status.uppercase())
                    _shelf.value = shelves.find { it.id == shelfIdParam }?.name ?: "None"
                }
            _mediaItemState.value = UIState.Success(item)
            } catch (e: Exception) {
                _mediaItemState.value = UIState.Error(R.string.unexpected_error)
            }
        }
    }

    /**
     * Returns all shelves returned by repository
     * @return list of shelf names
     */
    fun getShelves(): List<String> {
        return listOf("None") + shelves.map { it.name }
    }

    /**
     * Function which run after user changed the status.
     */
    fun onStatusSelected(status: MediaStatus) {
        _mediaStatus.value = status
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
        _shelf.value = shelf
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
        val shelfId = if (_shelf.value == "None") null else shelves.find { it.name == _shelf.value }?.id
        viewModelScope.launch {
            try {
                val item = (mediaItemState.value as? UIState.Success)?.data
                if (item != null) {
                    if (isEditParam) {
                        IShelfItemRepositary.updateShelfItem(itemId = item.id, shelfId = shelfId, status = mediaStatus.value.title, rating = rating.value, comment = comment.value)
                    } else {
                        IShelfItemRepositary.addShelfItem(id = item.id, mediaType = MediaType.valueOf(mediaTypeParam).title, status = mediaStatus.value.title, rating = rating.value, comment = comment.value.ifEmpty { "" }, shelf = shelf.value)
                    }
                }
                _state.emit(UIEvent.NavigateBack)
            } catch (e: Exception) {
                _state.emit(UIEvent.Error(R.string.unexpected_error))
            }
        }
    }

    companion object {
        /**
         * Factory for creating [AddEditItemViewModel] with dependencies from [PopshelfApplication].
        **/
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
