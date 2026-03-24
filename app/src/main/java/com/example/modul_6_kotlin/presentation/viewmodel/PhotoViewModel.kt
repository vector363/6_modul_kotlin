package com.example.modul_6_kotlin.presentation.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modul_6_kotlin.domain.model.Photo
import com.example.modul_6_kotlin.domain.usecase.DownloadPhotoUseCase
import com.example.modul_6_kotlin.domain.usecase.GetPhotosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PhotoUiState {
    object Loading : PhotoUiState()
    data class Success(val photos: List<Photo>) : PhotoUiState()
    data class Error(val message: String) : PhotoUiState()
}

sealed class DownloadState {
    object Idle : DownloadState()
    object Loading : DownloadState()
    data class Success(val message: String) : DownloadState()
    data class Error(val message: String) : DownloadState()
}

class PhotoViewModel(
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PhotoUiState>(PhotoUiState.Loading)
    val uiState: StateFlow<PhotoUiState> = _uiState.asStateFlow()

    private val _downloadState = MutableStateFlow<DownloadState>(DownloadState.Idle)
    val downloadState: StateFlow<DownloadState> = _downloadState.asStateFlow()

    private val downloadUseCase = DownloadPhotoUseCase()

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _uiState.value = PhotoUiState.Loading

            val result = getPhotosUseCase(page = 1, limit = 20)

            _uiState.value = when {
                result.isSuccess -> {
                    val photos = result.getOrNull() ?: emptyList()
                    if (photos.isEmpty()) {
                        PhotoUiState.Error("Фотографии не найдены")
                    } else {
                        PhotoUiState.Success(photos)
                    }
                }
                else -> {
                    val error = result.exceptionOrNull()
                    PhotoUiState.Error(error?.message ?: "Неизвестная ошибка")
                }
            }
        }
    }

    fun downloadPhoto(context: Context, photo: Photo) {
        viewModelScope.launch {
            _downloadState.value = DownloadState.Loading

            val result = downloadUseCase.downloadToMediaStore(context, photo)

            _downloadState.value = when {
                result.isSuccess -> {
                    DownloadState.Success("Фото сохранено в папку Загрузки")
                }
                else -> {
                    val error = result.exceptionOrNull()
                    DownloadState.Error(error?.message ?: "Не удалось сохранить фото")
                }
            }

            // Сбрасываем состояние через 3 секунды
            kotlinx.coroutines.delay(3000)
            _downloadState.value = DownloadState.Idle
        }
    }

    fun refresh() {
        loadPhotos()
    }

    fun clearDownloadState() {
        _downloadState.value = DownloadState.Idle
    }
}