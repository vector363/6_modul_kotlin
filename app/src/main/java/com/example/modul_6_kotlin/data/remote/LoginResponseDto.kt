package com.example.modul_6_kotlin.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val token: String,
    val userId: Int,
    val username: String,
    val email: String
)