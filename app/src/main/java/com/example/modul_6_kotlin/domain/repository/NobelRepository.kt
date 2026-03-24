package com.example.modul_6_kotlin.domain.repository

import com.example.modul_6_kotlin.domain.model.NobelPrize

interface NobelRepository {

    suspend fun getNobelPrizes(
        limit: Int = 25,
        offset: Int = 0,
        year: String? = null,
        category: String? = null
    ): Result<List<NobelPrize>>
}