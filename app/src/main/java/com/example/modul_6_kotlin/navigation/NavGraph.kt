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
import com.example.modul_6_kotlin.data.preferences.TokenManager
import com.example.modul_6_kotlin.data.repository.AuthRepositoryImpl
import com.example.modul_6_kotlin.presentation.ui.screen.LoginScreen
import com.example.modul_6_kotlin.presentation.ui.screen.NobelDetailScreen
import com.example.modul_6_kotlin.presentation.ui.screen.NobelListScreen
import com.example.modul_6_kotlin.presentation.viewmodel.LoginViewModel
import com.example.modul_6_kotlin.presentation.viewmodel.LoginViewModelFactory
import com.example.modul_6_kotlin.presentation.viewmodel.NobelUiState
import com.example.modul_6_kotlin.presentation.viewmodel.NobelViewModel
import com.example.modul_6_kotlin.presentation.viewmodel.NobelViewModelFactory


@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val tokenManager = remember { TokenManager(context) }
    val authRepository = AuthRepositoryImpl(tokenManager)

    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(authRepository)
    )

    val nobelViewModel: NobelViewModel = viewModel(
        factory = NobelViewModelFactory(authRepository)
    )

    val isAuthenticated = remember { authRepository.isAuthenticated() }
    val startDestination = if (isAuthenticated) "nobel_list" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate("nobel_list") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("nobel_list") {
            NobelListScreen(
                viewModel = nobelViewModel,
                onPrizeClick = { prize ->
                    navController.navigate("nobel_detail/${prize.id}")
                }
            )
        }

        composable(
            route = "nobel_detail/{prizeId}",
            arguments = listOf(navArgument("prizeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val prizeId = backStackEntry.arguments?.getInt("prizeId") ?: 0
            val prize = when (val state = nobelViewModel.uiState.value) {
                is NobelUiState.Success -> state.prizes.find { it.id == prizeId }
                else -> null
            }
            NobelDetailScreen(
                prize = prize,
                viewModel = nobelViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
