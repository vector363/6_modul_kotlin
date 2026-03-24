package com.example.modul_6_kotlin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.modul_6_kotlin.AuthApplication
import com.example.modul_6_kotlin.domain.model.User
import com.example.modul_6_kotlin.data.repository.AuthRepositoryImpl
import com.example.modul_6_kotlin.domain.usecase.GetUserDetailUseCase
import com.example.modul_6_kotlin.domain.usecase.GetUsersUseCase
import com.example.modul_6_kotlin.domain.usecase.LoginUseCase
import com.example.modul_6_kotlin.domain.usecase.LogoutUseCase
import com.example.modul_6_kotlin.presentation.ui.screen.LoginScreen
import com.example.modul_6_kotlin.presentation.ui.screen.UserDetailScreen
import com.example.modul_6_kotlin.presentation.ui.screen.UsersListScreen
import com.example.modul_6_kotlin.presentation.viewmodel.LoginViewModel
import com.example.modul_6_kotlin.presentation.viewmodel.LoginViewModelFactory
import com.example.modul_6_kotlin.presentation.viewmodel.UsersViewModel
import com.example.modul_6_kotlin.presentation.viewmodel.UsersViewModelFactory


@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as AuthApplication
    val tokenManager = application.tokenManager

    // Создаем репозиторий и use cases
    val repository = AuthRepositoryImpl(tokenManager)
    val loginUseCase = LoginUseCase(repository)
    val getUsersUseCase = GetUsersUseCase(repository)
    val getUserDetailUseCase = GetUserDetailUseCase(repository)
    val logoutUseCase = LogoutUseCase(repository)

    // Проверяем, авторизован ли пользователь
    val isAuthenticated = remember { repository.isAuthenticated() }
    val startDestination = if (isAuthenticated) "users_list" else "login"


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Экран логина
        composable("login") {
            val viewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(loginUseCase)
            )
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate("users_list") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Экран списка пользователей
        composable("users_list") {
            val viewModel: UsersViewModel = viewModel(
                factory = UsersViewModelFactory(
                    getUsersUseCase,
                    getUserDetailUseCase,
                    logoutUseCase
                )
            )
            UsersListScreen(
                viewModel = viewModel,
                onUserClick = { user: User ->
                    navController.navigate("user_detail/${user.id}")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("users_list") { inclusive = true }
                    }
                }
            )
        }

        // Экран детализации пользователя
        composable(
            route = "user_detail/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val viewModel: UsersViewModel = viewModel(
                factory = UsersViewModelFactory(
                    getUsersUseCase,
                    getUserDetailUseCase,
                    logoutUseCase
                )
            )
            UserDetailScreen(
                userId = userId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
