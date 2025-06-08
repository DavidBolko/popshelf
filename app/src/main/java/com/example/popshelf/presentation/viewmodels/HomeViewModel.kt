package com.example.popshelf.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.R
import com.example.popshelf.domain.Shelf
import com.example.popshelf.domain.repository.IShelfRepository
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel class for preserving and requesting data for the Home screen.
 *
 * @property state UI state representing list of shelves displayed on the home screen.
 * @property addShelfDialogState is add shelf dialog already open.
 * @constructor creates HomeViewModel with injected shelf repository.
 *
 * @param shelfRepository repository interface for accessing and managing shelves.
 */
class HomeViewModel(private val shelfRepository: IShelfRepository) : ViewModel() {
    private val _state = MutableStateFlow<UIState<List<Shelf>>>(UIState.Loading)
    val state: StateFlow<UIState<List<Shelf>>> = _state

    private val _addShelfDialogState = MutableStateFlow<Boolean>(false)
    val addShelfDialogState: StateFlow<Boolean> = _addShelfDialogState

    init {
        fetch()
    }

    private fun fetch(){
        viewModelScope.launch {
            try {
                shelfRepository.createDefaultShelves()
                _state.value = UIState.Success(shelfRepository.getAllShelves(true))
            } catch (e: Exception) {
                _state.value = UIState.Error(R.string.shelf_error)
            }
        }
    }

    /**
     * Function which runs after editing/adding or deleting work from a shelf, it's re-fetching the home screen data..
     */
    fun refresh() {
        fetch()
    }

    /**
     * Function which changes dialog open/close state
     */
    fun onDialogClick(state: Boolean){
        _addShelfDialogState.value = state
    }

    companion object {
        /**
         * Factory for creating [HomeViewModel] with dependencies from [PopshelfApplication].
         **/
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                HomeViewModel(app.shelfRepo)
            }
        }
    }
}