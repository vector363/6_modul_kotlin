package com.example.modul_6_kotlin.domain.usecase

import com.example.modul_6_kotlin.domain.repository.PhotoRepository
import com.example.modul_6_kotlin.domain.model.Photo

/**
 * Use Case для получения списка фотографий
 * Инкапсулирует бизнес-логику получения фото
 */
class GetPhotosUseCase(
    private val repository: PhotoRepository
) {
    /**
     * Выполнить получение фотографий
     * @param page номер страницы
     * @param limit количество фото на странице
     * @return Result<List<Photo>> — результат операции
     */
    suspend operator fun invoke(page: Int = 1, limit: Int = 20): Result<List<Photo>> {
        // Здесь можно добавить бизнес-логику:
        // - валидация параметров
        // - кэширование
        // - преобразование данных
        // - и т.д.

        return repository.getPhotos(page, limit)
    }
}