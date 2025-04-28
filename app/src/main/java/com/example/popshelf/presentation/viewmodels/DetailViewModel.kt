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
import com.example.popshelf.domain.useCases.GetMediaDetailUseCase
import com.example.popshelf.presentation.MediaType
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val getMediaDetailUseCase: GetMediaDetailUseCase, private val savedStateHandle: SavedStateHandle): ViewModel() {
    private val state = MutableStateFlow<UIState<MediaItem>>(UIState.Loading)
    val data: StateFlow<UIState<MediaItem>> = state

    private val id: String = savedStateHandle["id"] ?: ""
    val mediaType: String = savedStateHandle["mediaType"] ?: "Books"
    val fromShelf: Boolean = savedStateHandle["fromShelf"] ?: false
    init {
        viewModelScope.launch {
            try {
                val item = getMediaDetailUseCase.execute(MediaType.valueOf(mediaType), id)
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
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PopshelfApplication).appContainer
                DetailViewModel(app.getMediaDetailUseCase,  savedStateHandle)
            }
        }
    }
}