package com.example.modul_6_kotlin.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.modul_6_kotlin.presentation.viewmodel.UserDetailUiState
import com.example.modul_6_kotlin.presentation.viewmodel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: Int,
    viewModel: UsersViewModel,
    onBack: () -> Unit
) {
    val userDetailState by viewModel.userDetailState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getUserDetail(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали пользователя") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.resetUserDetailState()
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (userDetailState) {
                is UserDetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UserDetailUiState.Success -> {
                    val user = (userDetailState as UserDetailUiState.Success).user

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Аватар
                        AsyncImage(
                            model = user.image,
                            contentDescription = "Avatar of ${user.fullName}",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Информация
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "${user.fullName}",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "@${user.username}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${user.email}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "ID: ${user.id}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }

                is UserDetailUiState.Error -> {
                    val errorMessage = (userDetailState as UserDetailUiState.Error).message
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$errorMessage",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(onClick = { viewModel.getUserDetail(userId) }) {
                            Text("Повторить")
                        }
                    }
                }

                else -> {}
            }
        }
    }
}