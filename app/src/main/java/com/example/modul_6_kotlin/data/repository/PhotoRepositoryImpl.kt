package com.example.modul_6_kotlin.data.repository

import com.example.modul_6_kotlin.data.model.PhotoMapper
import com.example.modul_6_kotlin.data.remote.RetrofitClient
import com.example.modul_6_kotlin.domain.model.Photo
import com.example.modul_6_kotlin.domain.repository.PhotoRepository
import java.io.IOException

class PhotoRepositoryImpl : PhotoRepository {

    override suspend fun getPhotos(page: Int, limit: Int): Result<List<Photo>> {
        return try {
            // Запрос к API
            val dtos = RetrofitClient.apiService.getPhotos(page, limit)

            // Преобразование DTO → Domain Model
            val photos = PhotoMapper.mapToDomainList(dtos)

            Result.success(photos)
        } catch (e: IOException) {
            // Ошибка сети (нет интернета, таймаут и т.д.)
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: Exception) {
            // Другие ошибки (неправильный ответ сервера и т.д.)
            Result.failure(Exception("Ошибка загрузки: ${e.message}"))
        }
    }
}