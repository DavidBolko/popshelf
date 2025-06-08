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
import com.example.popshelf.domain.repository.IShelfRepository
import com.example.popshelf.presentation.MediaType
import com.example.popshelf.presentation.UIEvent
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel class responsible for preserving and requesting data for the Shelf screen.
 *
 * @property data ui state containing list of media items loaded from the shelf item repository.
 * @property state tells the UI it should navigate or some UI event error happened.
 * @property name shelf name retrieved from SavedStateHandle (nav params).
 * @property isSystem tells if the shelf is a system shelf or user.
 * @constructor creates ShelfViewModel with injected repositories and saved state handle (nav params).
 *
 * @param shelfItemRepository repository interface for accessing shelf items.
 * @param shelfRepository repository interface for managing shelves.
 * @param savedStateHandle class for retrieving navigation arguments.
 */
class ShelfViewModel(private val shelfItemRepository: IShelfItemRepositary, private val shelfRepository: IShelfRepository, savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _data = MutableStateFlow<UIState<List<MediaItem>>>(UIState.Loading)
    val data: StateFlow<UIState<List<MediaItem>>> = _data

    private val _state = MutableSharedFlow<UIEvent>()
    val state: SharedFlow<UIEvent> = _state

    private val id: String = savedStateHandle["id"] ?: ""
    val name: String = savedStateHandle["name"] ?: "Shelf"
    val isSystem: Boolean = MediaType.entries.any { it.name.equals(name, ignoreCase = true) }

    init {
        viewModelScope.launch {
            try {
                _data.value = UIState.Success(shelfItemRepository.getShelfItems(id.toInt()))
            } catch (e: Exception) {
                _data.value = UIState.Error(R.string.unexpected_item_error)
            }
        }
    }

    /**
     * Method which runs after clicking delete button in shelf screen
     */
    fun deleteShelf(){
        if(!isSystem){
            val itemIds = (_data.value as? UIState.Success)?.data?.map { it.id } ?: return
            viewModelScope.launch {
                try {
                    shelfItemRepository.deleteItemsFromShelves(itemIds)
                    shelfRepository.deleteShelf(id)
                    _state.emit(UIEvent.NavigateBack)
                } catch (e: Exception) {
                    _state.emit(UIEvent.Error(R.string.unexpected_error))
                }
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
                ShelfViewModel(app.shelfItemRepo, app.shelfRepo, savedStateHandle)
            }
        }
    }
}