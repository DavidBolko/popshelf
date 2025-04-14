package com.example.popshelf.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.popshelf.PopshelfApplication
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.domain.useCases.GetBookDetailUseCase
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val useCase: GetBookDetailUseCase, private val savedStateHandle: SavedStateHandle): ViewModel() {
    private val state = MutableStateFlow<UIState<MediaItem>>(UIState.Loading)
    val data: StateFlow<UIState<MediaItem>> = state

    private val id: String = savedStateHandle["id"] ?: ""
    init {
        viewModelScope.launch {
            try {
                val item = useCase.execute(id)
                state.value = UIState.Success(item)
            } catch (e: Exception) {
                state.value = UIState.Error("Chyba pri načítaní.")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication
                val repo = app.appContainer.BookRepo
                val useCase = GetBookDetailUseCase(repo)
                DetailViewModel(useCase, savedStateHandle)
            }
        }
    }
}