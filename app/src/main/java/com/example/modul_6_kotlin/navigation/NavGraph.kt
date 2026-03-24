package com.example.modul_6_kotlin.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modul_6_kotlin.data.repository.NobelRepositoryImpl
import com.example.modul_6_kotlin.domain.usecase.GetNobelPrizesUseCase
import com.example.modul_6_kotlin.presentation.ui.screen.NobelDetailScreen
import com.example.modul_6_kotlin.presentation.ui.screen.NobelListScreen
import com.example.modul_6_kotlin.presentation.viewmodel.NobelUiState
import com.example.modul_6_kotlin.presentation.viewmodel.NobelViewModel
import com.example.modul_6_kotlin.presentation.viewmodel.NobelViewModelFactory


@Composable
fun NavGraph(){
    val navController = rememberNavController()

    val repository = NobelRepositoryImpl()
    val getNobelPrizesUseCase = GetNobelPrizesUseCase(repository)

    val viewModel: NobelViewModel = viewModel(
        factory = NobelViewModelFactory(getNobelPrizesUseCase)
    )

    NavHost(
        navController = navController,
        startDestination = "nobel_list"
    ) {
        composable("nobel_list") {
            NobelListScreen(
                viewModel = viewModel,
                onPrizeClick = { prize ->
                    navController.navigate("nobel_detail/${prize.awardYear}_${prize.category}")
                }
            )
        }

        composable("nobel_detail/{prizeKey}") { backStackEntry ->
            val prizeKey = backStackEntry.arguments?.getString("prizeKey") ?: ""
            val prize = when (val state = viewModel.uiState.value) {
                is NobelUiState.Success -> state.prizes.find {
                    "${it.awardYear}_${it.category}" == prizeKey
                }
                else -> null
            }
            NobelDetailScreen(
                prize = prize,
                onBack = { navController.popBackStack() }
            )
        }

    }
}
