package com.example.modul_6_kotlin.domain.repository

import com.example.modul_6_kotlin.domain.model.Photo


interface PhotoRepository {
    suspend fun getPhotos(limit: Int): Result<List<Photo>>
}