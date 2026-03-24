package com.example.modul_6_kotlin.domain.usecase

import com.example.modul_6_kotlin.domain.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.clearToken()
    }
}