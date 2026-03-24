package com.example.modul_6_kotlin.data.remote

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("image")
    val image: String
)