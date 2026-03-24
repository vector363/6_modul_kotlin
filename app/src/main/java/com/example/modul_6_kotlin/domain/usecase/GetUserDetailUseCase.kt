package com.example.modul_6_kotlin.domain.usecase

import com.example.modul_6_kotlin.domain.model.User
import com.example.modul_6_kotlin.domain.repository.AuthRepository

class GetUserDetailUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(userId: Int): Result<User> {
        return repository.getUserById(userId)
    }
}