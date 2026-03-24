package com.example.modul_6_kotlin.data.remote

import com.google.gson.annotations.SerializedName

data class UsersResponseDto(
    @SerializedName("users")
    val users: List<UserDto>
)