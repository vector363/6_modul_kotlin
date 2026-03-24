package com.example.modul_6_kotlin.domain.model

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val image: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}