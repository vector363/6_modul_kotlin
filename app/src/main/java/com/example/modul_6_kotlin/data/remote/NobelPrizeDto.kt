package com.example.modul_6_kotlin.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NobelPrizeDto(
    @SerialName("id")
    val id: Int,
    @SerialName("year")
    val year: String,
    @SerialName("category")
    val category: String,
    @SerialName("prizeAmount")
    val prizeAmount: Int?,
    @SerialName("dateAwarded")
    val dateAwarded: String?,
    @SerialName("laureates")
    val laureates: List<LaureateDto>
)

@Serializable
data class LaureateDto(
    @SerialName("id")
    val id: String,
    @SerialName("fullName")
    val fullName: String,
    @SerialName("motivation")
    val motivation: String,
    @SerialName("portion")
    val portion: String,
    @SerialName("birthDate")
    val birthDate: String?,
    @SerialName("birthPlace")
    val birthPlace: String?
)