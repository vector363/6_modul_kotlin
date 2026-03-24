package com.example.modul_6_kotlin.domain.usecase

import com.example.modul_6_kotlin.data.model.LoginCredentials
import com.example.modul_6_kotlin.domain.model.User
import com.example.modul_6_kotlin.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        val credentials = LoginCredentials(username, password)
        return repository.login(credentials)
    }
}