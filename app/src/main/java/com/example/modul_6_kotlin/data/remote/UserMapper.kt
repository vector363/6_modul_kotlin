package com.example.modul_6_kotlin.data.remote

import com.example.modul_6_kotlin.domain.model.User


object UserMapper {

    fun mapToDomain(dto: UserDto): User {
        return User(
            id = dto.id,
            firstName = dto.firstName,
            lastName = dto.lastName,
            username = dto.username,
            email = dto.email,
            image = dto.image
        )
    }

    fun mapToDomainList(dtos: List<UserDto>): List<User> {
        return dtos.map { mapToDomain(it) }
    }

    fun mapToDomain(loginResponse: LoginResponseDto): User {
        return User(
            id = loginResponse.id,
            firstName = loginResponse.firstName,
            lastName = loginResponse.lastName,
            username = loginResponse.username,
            email = loginResponse.email,
            image = loginResponse.image
        )
    }
}