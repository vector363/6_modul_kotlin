package com.example.modul_6_kotlin.domain.usecase

import com.example.modul_6_kotlin.domain.model.User
import com.example.modul_6_kotlin.domain.repository.AuthRepository

class GetUsersUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return repository.getUsers()
    }
}