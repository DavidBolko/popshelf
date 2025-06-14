package com.example.popshelf.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.R
import com.example.popshelf.domain.repository.IShelfRepository
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel class for preserving and managing UI state for the AddShelf screen.
 *
 * @property state UI state representing the result of the shelf creation process.
 * @property nameState current name input typed by the user.
 * @property selectedColorKey key representing the selected shelf color.
 * @property isCreationAllowed tells if the shelf can be created (name is not blank).
 * @constructor creates AddShelfViewModel with injected shelf repository.
 *
 * @param shelfRepository - repository interface used to access and manipulate local shelves.
 */
class AddShelfViewModel(private val shelfRepository: IShelfRepository): ViewModel() {
    private val _state = MutableStateFlow<UIState<Unit>>(UIState.Loading)
    val state: StateFlow<UIState<Unit>> = _state

    private val _nameState = MutableStateFlow("")
    val nameState: StateFlow<String> = _nameState.asStateFlow()

    private val _selectedColorKey = MutableStateFlow<String>("Pink")
    val selectedColorKey: StateFlow<String> = _selectedColorKey.asStateFlow()

    val isCreationAllowed: StateFlow<Boolean> = _nameState.map { it.isNotBlank() }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private fun reset() {
        _nameState.value = ""
        _selectedColorKey.value = "Pink"
        _state.value = UIState.Loading
    }

    /**
     * Function which run after user changed the name of shelf.
     */
    fun onNameChange(newName: String) {
        _nameState.value = newName
    }

    /**
     * Function which run after user changed the color of shelf.
     */
    fun onColorSelected(color: String) {
        _selectedColorKey.value = color
    }

    /**
     * Function which run after clicking the confirm button during adding a shelf.
     */
    fun createShelf() {
        viewModelScope.launch {

            try {
                shelfRepository.createShelf(nameState.value, selectedColorKey.value)
                _state.value = UIState.Success(Unit)
                reset()
            } catch (e: Exception) {
                _state.value = UIState.Error(R.string.shelf_create_error)
            }
        }
    }

    companion object {
        /**
         * Factory for creating [AddShelfViewModel] with dependencies from [PopshelfApplication].
         **/
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                AddShelfViewModel(app.shelfRepo)
            }
        }
    }
}