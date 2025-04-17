package com.example.popshelf.presentation.viewmodels

import android.util.Log
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
import com.example.popshelf.domain.useCases.GetGameDetailUseCase
import com.example.popshelf.presentation.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val getBookDetailUseCase: GetBookDetailUseCase, private val getGameDetailUseCase: GetGameDetailUseCase, private val savedStateHandle: SavedStateHandle): ViewModel() {
    private val state = MutableStateFlow<UIState<MediaItem>>(UIState.Loading)
    val data: StateFlow<UIState<MediaItem>> = state

    private val id: String = savedStateHandle["id"] ?: ""
    private val mediaType: String = savedStateHandle["mediaType"] ?: "Books"
    init {
        viewModelScope.launch {
            try {
                var item: MediaItem = when(mediaType){
                    "Books" -> getBookDetailUseCase.execute(id)
                    "Games" -> getGameDetailUseCase.execute(id)
                    else -> throw IllegalArgumentException("Unknown type of media: $mediaType")
                }
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
                val repoBook = app.appContainer.BookRepo
                val repoGame = app.appContainer.GameRepo
                val useCaseBook = GetBookDetailUseCase(repoBook)
                val useCaseGame = GetGameDetailUseCase(repoGame)
                DetailViewModel(useCaseBook, useCaseGame,  savedStateHandle)
            }
        }
    }
}