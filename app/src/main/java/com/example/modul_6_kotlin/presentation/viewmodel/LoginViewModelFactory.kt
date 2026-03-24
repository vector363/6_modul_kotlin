package com.example.modul_6_kotlin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.modul_6_kotlin.domain.usecase.LoginUseCase

class LoginViewModelFactory(
    private val loginUseCase: LoginUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}