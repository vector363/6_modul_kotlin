package com.example.modul_6_kotlin.domain.repository

import com.example.modul_6_kotlin.domain.model.Laureate
import com.example.modul_6_kotlin.domain.model.LoginCredentials
import com.example.modul_6_kotlin.domain.model.NobelPrize
import com.example.modul_6_kotlin.domain.model.User

interface AuthRepository {

    suspend fun login(credentials: LoginCredentials): Result<User>

    suspend fun register(username: String, email: String, password: String): Result<User>

    suspend fun getAllPrizes(): Result<List<NobelPrize>>

    suspend fun getPrizeByYearAndCategory(year: String, category: String): Result<NobelPrize>

    suspend fun getLaureatesByPrize(year: String, category: String): Result<List<Laureate>>

    suspend fun getUserFavorites(): Result<List<NobelPrize>>

    suspend fun addToFavorites(prizeId: Int): Result<Unit>

    suspend fun removeFromFavorites(prizeId: Int): Result<Unit>

    suspend fun saveToken(token: String)

    suspend fun getToken(): String?

    suspend fun clearToken()

    fun isAuthenticated(): Boolean
}
