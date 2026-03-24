package com.example.modul_6_kotlin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.modul_6_kotlin.domain.usecase.GetUserDetailUseCase
import com.example.modul_6_kotlin.domain.usecase.GetUsersUseCase
import com.example.modul_6_kotlin.domain.usecase.LogoutUseCase

class UsersViewModelFactory(
    private val getUsersUseCase: GetUsersUseCase,
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(
                getUsersUseCase,
                getUserDetailUseCase,
                logoutUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}