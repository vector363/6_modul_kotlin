package com.example.modul_6_kotlin.data.model

import com.example.modul_6_kotlin.domain.model.Photo

object PhotoMapper {

    fun mapToDomain(dto: PhotoDto): Photo {
        return Photo(
            id = dto.id,
            author = dto.author,
            width = dto.width,
            height = dto.height,
            url = dto.url,
            downloadUrl = dto.downloadUrl
        )
    }

    fun mapToDomainList(dtos: List<PhotoDto>): List<Photo> {
        return dtos.map { mapToDomain(it) }
    }
}