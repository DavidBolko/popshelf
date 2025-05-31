package com.example.popshelf.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.domain.Shelf
import com.example.popshelf.domain.repository.ShelfRepositary
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/***
 * Viewmodel class for preserving and requesting data for HomeViewModel
 * @author David Bolko
 * @property shelfRepositary - repository for items of individual shelves.
 */
class HomeViewModel(private val shelfRepositary: ShelfRepositary) : ViewModel() {
    private val _state = MutableStateFlow<UIState<List<Shelf>>>(UIState.Loading)
    val state: StateFlow<UIState<List<Shelf>>> = _state

    init {
        fetch()
    }

    private fun fetch(){
        viewModelScope.launch {
            try {
                shelfRepositary.createDefaultShelves()
                _state.value = UIState.Success(shelfRepositary.getAllShelves(true))
            } catch (e: Exception) {
                _state.value = UIState.Error("Chyba pri načítaní poličiek.")
            }
        }
    }

    /**
     * Function which runs after editing/adding or deleting work from a shelf, it's re-fetching the home screen data..
     */
    fun refresh() {
        fetch()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                HomeViewModel(app.shelfRepo)
            }
        }
    }
}