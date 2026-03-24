package com.example.modul_6_kotlin.data.repository

import com.example.modul_6_kotlin.data.api.NobelApiService
import com.example.modul_6_kotlin.data.remote.KtorClient
import com.example.modul_6_kotlin.data.remote.NobelPrizeMapper
import com.example.modul_6_kotlin.domain.model.NobelPrize
import com.example.modul_6_kotlin.domain.repository.NobelRepository
import java.io.IOException

class NobelRepositoryImpl : NobelRepository {

    private val apiService = NobelApiService(
        client = KtorClient.client,
        baseUrl = KtorClient.getBaseUrl()
    )

    override suspend fun getNobelPrizes(
        limit: Int,
        offset: Int,
        year: String?,
        category: String?
    ): Result<List<NobelPrize>> {
        return try {
            val response = apiService.getNobelPrizes(limit, offset, year, category)
            val prizes = NobelPrizeMapper.mapToDomainList(response.nobelPrizes)
            Result.success(prizes)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка загрузки: ${e.message}"))
        }
    }
}