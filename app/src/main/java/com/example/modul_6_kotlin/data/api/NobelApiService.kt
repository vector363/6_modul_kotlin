package com.example.modul_6_kotlin.data.api

import com.example.modul_6_kotlin.data.remote.LaureateDto
import com.example.modul_6_kotlin.data.remote.LoginRequestDto
import com.example.modul_6_kotlin.data.remote.LoginResponseDto
import com.example.modul_6_kotlin.data.remote.NobelPrizeDto
import com.example.modul_6_kotlin.data.remote.RegisterRequestDto
import retrofit2.http.*


interface NobelApiService {

    // Авторизация
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): LoginResponseDto

    // Премии (защищенные)
    @GET("prizes")
    suspend fun getAllPrizes(): List<NobelPrizeDto>

    @GET("prizes/{year}/{category}")
    suspend fun getPrizeByYearAndCategory(
        @Path("year") year: String,
        @Path("category") category: String
    ): NobelPrizeDto

    @GET("prizes/{year}/{category}/laureates")
    suspend fun getLaureatesByPrize(
        @Path("year") year: String,
        @Path("category") category: String
    ): List<LaureateDto>

    // Избранное
    @GET("users/me/prizes")
    suspend fun getUserFavorites(): List<NobelPrizeDto>

    @POST("users/me/prizes/{prizeId}")
    suspend fun addToFavorites(
        @Path("prizeId") prizeId: Int
    ): Map<String, String>

    @DELETE("users/me/prizes/{prizeId}")
    suspend fun removeFromFavorites(
        @Path("prizeId") prizeId: Int
    ): Map<String, String>
}