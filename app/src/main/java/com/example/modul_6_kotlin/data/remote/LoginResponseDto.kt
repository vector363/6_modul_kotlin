package com.example.modul_6_kotlin.data.remote

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)