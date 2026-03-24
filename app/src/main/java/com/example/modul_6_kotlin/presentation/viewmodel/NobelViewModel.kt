package com.example.modul_6_kotlin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modul_6_kotlin.domain.model.NobelPrize
import com.example.modul_6_kotlin.domain.usecase.GetNobelPrizesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NobelUiState {
    object Loading : NobelUiState()
    data class Success(val prizes: List<NobelPrize>) : NobelUiState()
    data class Error(val message: String) : NobelUiState()
}

class NobelViewModel(
    private val getNobelPrizesUseCase: GetNobelPrizesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NobelUiState>(NobelUiState.Loading)
    val uiState: StateFlow<NobelUiState> = _uiState.asStateFlow()

    // Список доступных категорий для фильтра
    val categories = listOf(
        "chemistry", "physics", "medicine", "literature", "peace", "economics"
    )

    private var currentYear: String? = null
    private var currentCategory: String? = null

    init {
        loadPrizes()
    }

    fun loadPrizes(year: String? = currentYear, category: String? = currentCategory) {
        currentYear = year
        currentCategory = category

        viewModelScope.launch {
            _uiState.value = NobelUiState.Loading

            val result = getNobelPrizesUseCase(
                limit = 50,
                offset = 0,
                year = year,
                category = category
            )

            _uiState.value = when {
                result.isSuccess -> {
                    val prizes = result.getOrNull() ?: emptyList()
                    if (prizes.isEmpty()) {
                        NobelUiState.Error("Нет данных для выбранных фильтров")
                    } else {
                        NobelUiState.Success(prizes)
                    }
                }
                else -> {
                    val error = result.exceptionOrNull()
                    NobelUiState.Error(error?.message ?: "Неизвестная ошибка")
                }
            }
        }
    }

    fun filterByYear(year: String?) {
        loadPrizes(year = year, category = currentCategory)
    }

    fun filterByCategory(category: String?) {
        loadPrizes(year = currentYear, category = category)
    }

    fun clearFilters() {
        currentYear = null
        currentCategory = null
        loadPrizes()
    }
}