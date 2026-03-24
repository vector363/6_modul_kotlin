package com.example.modul_6_kotlin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modul_6_kotlin.domain.model.User
import com.example.modul_6_kotlin.domain.usecase.GetUserDetailUseCase
import com.example.modul_6_kotlin.domain.usecase.GetUsersUseCase
import com.example.modul_6_kotlin.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UsersUiState {
    object Loading : UsersUiState()
    data class Success(val users: List<User>) : UsersUiState()
    data class Error(val message: String) : UsersUiState()
}

sealed class UserDetailUiState {
    object Idle : UserDetailUiState()
    object Loading : UserDetailUiState()
    data class Success(val user: User) : UserDetailUiState()
    data class Error(val message: String) : UserDetailUiState()
}

class UsersViewModel(
    private val getUsersUseCase: GetUsersUseCase,
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _usersState = MutableStateFlow<UsersUiState>(UsersUiState.Loading)
    val usersState: StateFlow<UsersUiState> = _usersState.asStateFlow()

    private val _userDetailState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Idle)
    val userDetailState: StateFlow<UserDetailUiState> = _userDetailState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = UsersUiState.Loading

            val result = getUsersUseCase()

            _usersState.value = when {
                result.isSuccess -> {
                    val users = result.getOrNull() ?: emptyList()
                    UsersUiState.Success(users)
                }
                else -> {
                    val error = result.exceptionOrNull()
                    UsersUiState.Error(error?.message ?: "Ошибка загрузки")
                }
            }
        }
    }

    fun getUserDetail(userId: Int) {
        viewModelScope.launch {
            _userDetailState.value = UserDetailUiState.Loading

            val result = getUserDetailUseCase(userId)

            _userDetailState.value = when {
                result.isSuccess -> {
                    val user = result.getOrNull()
                    UserDetailUiState.Success(user!!)
                }
                else -> {
                    val error = result.exceptionOrNull()
                    UserDetailUiState.Error(error?.message ?: "Ошибка загрузки")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    fun resetUserDetailState() {
        _userDetailState.value = UserDetailUiState.Idle
    }
}