package com.example.modul_6_kotlin.data.remote

import com.example.modul_6_kotlin.domain.model.Laureate
import com.example.modul_6_kotlin.domain.model.NobelPrize

object NobelPrizeMapper {

    fun mapToDomain(dto: NobelPrizeDto): NobelPrize {
        return NobelPrize(
            id = dto.id,
            awardYear = dto.year,
            category = dto.category,
            dateAwarded = dto.dateAwarded,
            prizeAmount = dto.prizeAmount,
            laureates = dto.laureates.map { mapLaureateToDomain(it) }
        )
    }

    fun mapLaureateToDomain(dto: LaureateDto): Laureate {
        return Laureate(
            id = dto.id,
            fullName = dto.fullName,
            motivation = dto.motivation,
            portion = dto.portion,
            birthDate = dto.birthDate,
            birthPlace = dto.birthPlace
        )
    }

    fun mapToDomainList(dtos: List<NobelPrizeDto>): List<NobelPrize> {
        return dtos.map { mapToDomain(it) }
    }
}