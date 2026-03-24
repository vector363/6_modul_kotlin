package com.example.modul_6_kotlin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.modul_6_kotlin.domain.usecase.GetPhotosUseCase


class PhotoViewModelFactory(
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoViewModel(getPhotosUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}