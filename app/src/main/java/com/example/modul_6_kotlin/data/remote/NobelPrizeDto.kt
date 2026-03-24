package com.example.modul_6_kotlin.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NobelPrizeResponse(
    @SerialName("nobelPrizes")
    val nobelPrizes: List<NobelPrizeDto>
)

@Serializable
data class NobelPrizeDto(
    @SerialName("awardYear")
    val awardYear: String,
    @SerialName("category")
    val category: CategoryDto,
    @SerialName("dateAwarded")
    val dateAwarded: String?,
    @SerialName("prizeAmount")
    val prizeAmount: Int?,
    @SerialName("laureates")
    val laureates: List<LaureateDto>?
)

@Serializable
data class CategoryDto(
    @SerialName("en")
    val en: String? = null,
    @SerialName("no")
    val no: String? = null,
    @SerialName("se")
    val se: String? = null
)

@Serializable
data class LaureateDto(
    @SerialName("id")
    val id: String,
    @SerialName("fullName")
    val fullName: FullNameDto? = null,
    @SerialName("knownName")
    val knownName: KnownNameDto? = null,
    @SerialName("orgName")
    val orgName: OrgNameDto? = null,
    @SerialName("portion")
    val portion: String,
    @SerialName("sortOrder")
    val sortOrder: String,
    @SerialName("motivation")
    val motivation: MotivationDto? = null,
    @SerialName("birth")
    val birth: BirthDto? = null
)

@Serializable
data class FullNameDto(
    @SerialName("en")
    val en: String? = null
)

@Serializable
data class KnownNameDto(
    @SerialName("en")
    val en: String? = null
)

@Serializable
data class OrgNameDto(
    @SerialName("en")
    val en: String? = null
)

@Serializable
data class MotivationDto(
    @SerialName("en")
    val en: String? = null
)

@Serializable
data class BirthDto(
    @SerialName("date")
    val date: String? = null,
    @SerialName("place")
    val place: PlaceDto? = null
)

@Serializable
data class PlaceDto(
    @SerialName("city")
    val city: String? = null,
    @SerialName("country")
    val country: String? = null
)