package com.example.modul_6_kotlin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.modul_6_kotlin.domain.usecase.GetNobelPrizesUseCase

class NobelViewModelFactory(
    private val getNobelPrizesUseCase: GetNobelPrizesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NobelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NobelViewModel(getNobelPrizesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}