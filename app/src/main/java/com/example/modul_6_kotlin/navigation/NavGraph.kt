package com.example.modul_6_kotlin.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modul_6_kotlin.data.repository.PhotoRepositoryImpl
import com.example.modul_6_kotlin.domain.usecase.GetPhotosUseCase
import com.example.modul_6_kotlin.presentation.ui.screen.PhotoDetailScreen
import com.example.modul_6_kotlin.presentation.ui.screen.PhotoListScreen
import com.example.modul_6_kotlin.presentation.viewmodel.PhotoUiState
import com.example.modul_6_kotlin.presentation.viewmodel.PhotoViewModel
import com.example.modul_6_kotlin.presentation.viewmodel.PhotoViewModelFactory


@Composable
fun NavGraph(){
    val navController = rememberNavController()

    // Создаем UseCase
    val repository = PhotoRepositoryImpl()
    val getPhotosUseCase = GetPhotosUseCase(repository)

    val viewModel: PhotoViewModel = viewModel(
        factory = PhotoViewModelFactory(getPhotosUseCase)
    )

    NavHost(
        navController = navController,
        startDestination = "photo_list"
    ) {
        composable("photo_list") {
            PhotoListScreen(
                viewModel = viewModel,
                onPhotoClick = { photo ->
                    navController.navigate("photo_detail/${photo.id}")
                }
            )
        }
        composable("photo_detail/{photoId}") { backStackEntry ->
            val photoId = backStackEntry.arguments?.getString("photoId") ?: ""
            // Получаем фото из ViewModel
            val photo = viewModel.uiState.value.let { state ->
                if (state is PhotoUiState.Success) {
                    state.photos.find { it.id == photoId }
                } else null
            }
            PhotoDetailScreen(
                photo = photo,
                viewModel = viewModel,  // ← это было пропущено
                onBack = { navController.popBackStack() }
            )
        }
    }
}
