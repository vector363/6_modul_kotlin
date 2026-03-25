package com.example.modul_6_kotlin.domain.model

data class NobelPrize(
    val id: Int,
    val awardYear: String,
    val category: String,
    val dateAwarded: String?,
    val prizeAmount: Int?,
    val laureates: List<Laureate>
)

data class Laureate(
    val id: String,
    val fullName: String,
    val motivation: String,
    val birthDate: String?,
    val birthPlace: String?,
    val portion: String?
)