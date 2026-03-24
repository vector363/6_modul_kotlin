package com.example.modul_6_kotlin.data.api

import com.example.modul_6_kotlin.data.remote.LoginRequestDto
import com.example.modul_6_kotlin.data.remote.LoginResponseDto
import com.example.modul_6_kotlin.data.remote.UsersResponseDto
import retrofit2.http.*

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto

    @GET("users")
    suspend fun getUsers(): UsersResponseDto

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: Int
    ): LoginResponseDto  // структура та же, что и при логине
}