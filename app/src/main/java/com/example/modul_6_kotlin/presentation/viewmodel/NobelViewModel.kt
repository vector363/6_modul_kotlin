package com.example.modul_6_kotlin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modul_6_kotlin.domain.model.NobelPrize
import com.example.modul_6_kotlin.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NobelUiState {
    object Loading : NobelUiState()
    data class Success(val prizes: List<NobelPrize>) : NobelUiState()
    data class Error(val message: String) : NobelUiState()
}

sealed class FavoriteActionState {
    object Idle : FavoriteActionState()
    object Loading : FavoriteActionState()
    data class Success(val message: String) : FavoriteActionState()
    data class Error(val message: String) : FavoriteActionState()
}

class NobelViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NobelUiState>(NobelUiState.Loading)
    val uiState: StateFlow<NobelUiState> = _uiState.asStateFlow()

    private val _favoriteState = MutableStateFlow<FavoriteActionState>(FavoriteActionState.Idle)
    val favoriteState: StateFlow<FavoriteActionState> = _favoriteState.asStateFlow()

    private val _favorites = MutableStateFlow<List<NobelPrize>>(emptyList())
    val favorites: StateFlow<List<NobelPrize>> = _favorites.asStateFlow()

    private var currentPrizes: List<NobelPrize> = emptyList()

    init {
        loadPrizes()
        loadFavorites()
    }

    fun loadPrizes() {
        viewModelScope.launch {
            _uiState.value = NobelUiState.Loading

            val result = authRepository.getAllPrizes()

            _uiState.value = when {
                result.isSuccess -> {
                    currentPrizes = result.getOrNull() ?: emptyList()
                    NobelUiState.Success(currentPrizes)
                }
                else -> {
                    val error = result.exceptionOrNull()
                    NobelUiState.Error(error?.message ?: "Неизвестная ошибка")
                }
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            val result = authRepository.getUserFavorites()
            if (result.isSuccess) {
                _favorites.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    fun isFavorite(prizeId: Int): Boolean {
        return _favorites.value.any { it.id == prizeId }
    }

    fun toggleFavorite(prize: NobelPrize) {
        viewModelScope.launch {
            _favoriteState.value = FavoriteActionState.Loading

            val result = if (isFavorite(prize.id)) {
                authRepository.removeFromFavorites(prize.id)
            } else {
                authRepository.addToFavorites(prize.id)
            }

            _favoriteState.value = when {
                result.isSuccess -> {
                    // Обновляем список избранного
                    loadFavorites()
                    FavoriteActionState.Success(
                        if (isFavorite(prize.id)) "Удалено из избранного" else "Добавлено в избранное"
                    )
                }
                else -> {
                    val error = result.exceptionOrNull()
                    FavoriteActionState.Error(error?.message ?: "Ошибка")
                }
            }

            // Сбрасываем состояние через 3 секунды
            kotlinx.coroutines.delay(3000)
            _favoriteState.value = FavoriteActionState.Idle
        }
    }

    fun refresh() {
        loadPrizes()
        loadFavorites()
    }

    fun clearFavoriteState() {
        _favoriteState.value = FavoriteActionState.Idle
    }
}