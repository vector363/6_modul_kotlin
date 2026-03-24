package com.example.modul_6_kotlin.domain.usecase

import com.example.modul_6_kotlin.domain.model.NobelPrize
import com.example.modul_6_kotlin.domain.repository.NobelRepository

class GetNobelPrizesUseCase(
    private val repository: NobelRepository
) {
    suspend operator fun invoke(
        limit: Int = 25,
        offset: Int = 0,
        year: String? = null,
        category: String? = null
    ): Result<List<NobelPrize>> {
        return repository.getNobelPrizes(limit, offset, year, category)
    }
}