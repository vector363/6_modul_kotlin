package com.example.modul_6_kotlin.data.repository

import com.example.modul_6_kotlin.data.model.PhotoMapper
import com.example.modul_6_kotlin.data.remote.RetrofitClient
import com.example.modul_6_kotlin.domain.model.Photo
import com.example.modul_6_kotlin.domain.repository.PhotoRepository
import java.io.IOException

class PhotoRepositoryImpl : PhotoRepository {
    override suspend fun getPhotos(limit: Int): Result<List<Photo>> {
        return try {
            val dtos = RetrofitClient.apiService.getPhotos(limit)
            val photos = PhotoMapper.mapToDomainList(dtos)

            Result.success(photos)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка загрузки: ${e.message}"))
        }
    }
}