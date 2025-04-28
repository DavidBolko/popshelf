package com.example.popshelf.presentation.viewmodels

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.domain.repository.ShelfRepositary
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddShelfViewModel(private val repositary: ShelfRepositary): ViewModel() {
    private val _state = MutableStateFlow<UIState<Unit>>(UIState.Loading)
    val state: StateFlow<UIState<Unit>> = _state

    private val _nameState = MutableStateFlow("")
    val nameState: StateFlow<String> = _nameState.asStateFlow()

    private val _selectedColorKey = MutableStateFlow<String>("Pink")
    val selectedColorKey: StateFlow<String> = _selectedColorKey.asStateFlow()

    fun onColorSelected(color: String) {
        _selectedColorKey.value = color
    }

    val isCreationAllowed: StateFlow<Boolean> = _nameState.map { it.isNotBlank() }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun onNameChange(newName: String) {
        _nameState.value = newName
    }

    private fun reset() {
        _nameState.value = ""
        _selectedColorKey.value = "Pink"
        _state.value = UIState.Loading
    }

    fun createShelf() {
        viewModelScope.launch {
            if (nameState.value.isBlank()) {
                _state.value = UIState.Error("Názov nemôže byť prázdny.")
                return@launch
            }

            try {
                repositary.createShelf(nameState.value, selectedColorKey.value)
                _state.value = UIState.Success(Unit)
                reset()
            } catch (e: Exception) {
                _state.value = UIState.Error("Nepodarilo sa vytvoriť poličku.")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                AddShelfViewModel(app.ShelfRepo)
            }
        }
    }
}