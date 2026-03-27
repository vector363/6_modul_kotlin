package com.example.modul_6_kotlin.domain.usecase

import com.example.modul_6_kotlin.domain.repository.PhotoRepository
import com.example.modul_6_kotlin.domain.model.Photo


class GetPhotosUseCase(
    private val repository: PhotoRepository
) {
    suspend operator fun invoke(limit: Int): Result<List<Photo>> {
        return repository.getPhotos(limit)
    }
}