package com.example.modul_6_kotlin.data.repository

import com.example.modul_6_kotlin.data.preferences.TokenManager
import com.example.modul_6_kotlin.data.remote.LoginRequestDto
import com.example.modul_6_kotlin.data.remote.NobelPrizeMapper
import com.example.modul_6_kotlin.data.remote.RegisterRequestDto
import com.example.modul_6_kotlin.data.remote.RetrofitClient
import com.example.modul_6_kotlin.domain.model.Laureate
import com.example.modul_6_kotlin.domain.model.LoginCredentials
import com.example.modul_6_kotlin.domain.model.NobelPrize
import com.example.modul_6_kotlin.domain.model.User
import com.example.modul_6_kotlin.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.IOException

class AuthRepositoryImpl(
    private val tokenManager: TokenManager
) : AuthRepository {

    private var cachedToken: String? = null

    override suspend fun login(credentials: LoginCredentials): Result<User> {
        return try {
            val client = RetrofitClient.createClient()
            val apiService = RetrofitClient.getApiService(client)

            val request = LoginRequestDto(
                username = credentials.username,
                password = credentials.password
            )

            val response = apiService.login(request)
            val user = User(
                id = response.userId,
                username = response.username,
                email = response.email
            )

            saveToken(response.token)
            cachedToken = response.token

            Result.success(user)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка авторизации: ${e.message}"))
        }
    }

    override suspend fun register(username: String, email: String, password: String): Result<User> {
        return try {
            val client = RetrofitClient.createClient()
            val apiService = RetrofitClient.getApiService(client)

            val request = RegisterRequestDto(username, email, password)
            val response = apiService.register(request)
            val user = User(
                id = response.userId,
                username = response.username,
                email = response.email
            )

            saveToken(response.token)
            cachedToken = response.token

            Result.success(user)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка регистрации: ${e.message}"))
        }
    }

    override suspend fun getAllPrizes(): Result<List<NobelPrize>> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("Не авторизован"))
            val client = RetrofitClient.createClient(token)
            val apiService = RetrofitClient.getApiService(client)

            val response = apiService.getAllPrizes()
            val prizes = response.map { NobelPrizeMapper.mapToDomain(it) }

            Result.success(prizes)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка загрузки: ${e.message}"))
        }
    }

    override suspend fun getPrizeByYearAndCategory(year: String, category: String): Result<NobelPrize> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("Не авторизован"))
            val client = RetrofitClient.createClient(token)
            val apiService = RetrofitClient.getApiService(client)

            val response = apiService.getPrizeByYearAndCategory(year, category)
            val prize = NobelPrizeMapper.mapToDomain(response)

            Result.success(prize)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка загрузки: ${e.message}"))
        }
    }

    override suspend fun getLaureatesByPrize(year: String, category: String): Result<List<Laureate>> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("Не авторизован"))
            val client = RetrofitClient.createClient(token)
            val apiService = RetrofitClient.getApiService(client)

            val response = apiService.getLaureatesByPrize(year, category)
            val laureates = response.map { NobelPrizeMapper.mapLaureateToDomain(it) }

            Result.success(laureates)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка загрузки: ${e.message}"))
        }
    }

    override suspend fun getUserFavorites(): Result<List<NobelPrize>> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("Не авторизован"))
            val client = RetrofitClient.createClient(token)
            val apiService = RetrofitClient.getApiService(client)

            val response = apiService.getUserFavorites()
            val prizes = response.map { NobelPrizeMapper.mapToDomain(it) }

            Result.success(prizes)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка загрузки: ${e.message}"))
        }
    }

    override suspend fun addToFavorites(prizeId: Int): Result<Unit> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("Не авторизован"))
            val client = RetrofitClient.createClient(token)
            val apiService = RetrofitClient.getApiService(client)

            apiService.addToFavorites(prizeId)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка: ${e.message}"))
        }
    }

    override suspend fun removeFromFavorites(prizeId: Int): Result<Unit> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("Не авторизован"))
            val client = RetrofitClient.createClient(token)
            val apiService = RetrofitClient.getApiService(client)

            apiService.removeFromFavorites(prizeId)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка: ${e.message}"))
        }
    }

    override suspend fun saveToken(token: String) {
        tokenManager.saveToken(token)
        cachedToken = token
    }

    override suspend fun getToken(): String? {
        return cachedToken ?: tokenManager.tokenFlow.first()
    }

    override suspend fun clearToken() {
        tokenManager.clearToken()
        cachedToken = null
    }

    override fun isAuthenticated(): Boolean {
        return runBlocking {
            getToken() != null
        }
    }
}