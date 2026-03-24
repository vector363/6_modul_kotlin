package com.example.modul_6_kotlin.domain.repository

import com.example.modul_6_kotlin.domain.model.Photo

/**
 * Интерфейс репозитория фотографий
 * Абстракция над источником данных (Retrofit)
 */
interface PhotoRepository {

    /**
     * Получить список фотографий
     * @param page номер страницы
     * @param limit количество фото на странице
     * @return Result<List<Photo>> — успех или ошибка
     */
    suspend fun getPhotos(page: Int, limit: Int): Result<List<Photo>>
}