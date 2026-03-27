package com.example.modul_6_kotlin.data.repository

import com.example.modul_6_kotlin.data.TokenManager
import com.example.modul_6_kotlin.data.model.LoginCredentials
import com.example.modul_6_kotlin.domain.model.User
import com.example.modul_6_kotlin.data.remote.LoginRequestDto
import com.example.modul_6_kotlin.data.remote.RetrofitClient
import com.example.modul_6_kotlin.data.remote.UserMapper
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
            val user = UserMapper.mapToDomain(response)

            saveToken(response.accessToken)
            cachedToken = response.accessToken

            Result.success(user)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка авторизации: ${e.message}"))
        }
    }

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("Не авторизован"))

            val client = RetrofitClient.createClient(token)
            val apiService = RetrofitClient.getApiService(client)

            val response = apiService.getUsers()
            val users = UserMapper.mapToDomainList(response.users)

            Result.success(users)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка загрузки: ${e.message}"))
        }
    }

    override suspend fun getUserById(userId: Int): Result<User> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("Не авторизован"))

            val client = RetrofitClient.createClient(token)
            val apiService = RetrofitClient.getApiService(client)

            val response = apiService.getUserById(userId)
            val user = UserMapper.mapToDomain(response)

            Result.success(user)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка загрузки: ${e.message}"))
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