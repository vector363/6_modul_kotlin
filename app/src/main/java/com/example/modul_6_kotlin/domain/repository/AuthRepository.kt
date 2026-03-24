package com.example.modul_6_kotlin.domain.repository

import com.example.modul_6_kotlin.data.model.LoginCredentials
import com.example.modul_6_kotlin.domain.model.User


interface AuthRepository {

    suspend fun login(credentials: LoginCredentials): Result<User>

    suspend fun getUsers(): Result<List<User>>

    suspend fun getUserById(userId: Int): Result<User>

    suspend fun saveToken(token: String)

    suspend fun getToken(): String?

    suspend fun clearToken()

    fun isAuthenticated(): Boolean
}