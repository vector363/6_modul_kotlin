package com.example.modul_6_kotlin.data.api

import com.example.modul_6_kotlin.data.remote.NobelPrizeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class NobelApiService(
    private val client: HttpClient,
    private val baseUrl: String
) {

    suspend fun getNobelPrizes(
        limit: Int = 25,
        offset: Int = 0,
        year: String? = null,
        category: String? = null
    ): NobelPrizeResponse {
        return client.get("${baseUrl}nobelPrizes") {
            parameter("limit", limit)
            parameter("offset", offset)
            year?.let { parameter("nobelPrizeYear", it) }
            category?.let { parameter("nobelPrizeCategory", it) }
        }.body()
    }
}