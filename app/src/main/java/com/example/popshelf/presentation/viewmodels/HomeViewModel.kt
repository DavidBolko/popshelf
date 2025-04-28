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


class HomeViewModel(private val repositary: ShelfRepositary) : ViewModel() {
    private val _state = MutableStateFlow<UIState<List<Shelf>>>(UIState.Loading)
    val state: StateFlow<UIState<List<Shelf>>> = _state

    fun fetch(){
        viewModelScope.launch {
            try {
                repositary.createDefaultShelves()
                _state.value = UIState.Success(repositary.getAllShelves(true))
            } catch (e: Exception) {
                _state.value = UIState.Error("Chyba pri načítaní poličiek.")
            }
        }
    }

    fun refresh() {
        fetch()
    }

    init {
        fetch()
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                HomeViewModel(app.ShelfRepo)
            }
        }
    }
}