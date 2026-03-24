package com.example.modul_6_kotlin.data.remote

import com.example.modul_6_kotlin.domain.model.Laureate
import com.example.modul_6_kotlin.domain.model.NobelPrize

object NobelPrizeMapper {

    fun mapToDomain(dto: NobelPrizeDto): NobelPrize {
        return NobelPrize(
            awardYear = dto.awardYear,
            category = dto.category.en ?: "Unknown",
            dateAwarded = dto.dateAwarded,
            prizeAmount = dto.prizeAmount,
            laureates = dto.laureates?.map { mapLaureateToDomain(it) } ?: emptyList()
        )
    }

    fun mapLaureateToDomain(dto: LaureateDto): Laureate {
        // Получаем имя (приоритет: fullName, knownName, orgName)
        val name = when {
            dto.fullName?.en != null -> dto.fullName.en
            dto.knownName?.en != null -> dto.knownName.en
            dto.orgName?.en != null -> dto.orgName.en
            else -> "Unknown"
        }

        // Получаем мотивацию
        val motivation = dto.motivation?.en ?: "Нет описания"

        // Формируем место рождения
        val birthPlace = dto.birth?.place?.let {
            listOfNotNull(it.city, it.country).joinToString(", ")
        } ?: "Не указано"

        return Laureate(
            id = dto.id,
            fullName = name,
            motivation = motivation,
            birthDate = dto.birth?.date,
            birthPlace = birthPlace,
            portion = dto.portion
        )
    }

    fun mapToDomainList(dtos: List<NobelPrizeDto>): List<NobelPrize> {
        return dtos.map { mapToDomain(it) }
    }
}