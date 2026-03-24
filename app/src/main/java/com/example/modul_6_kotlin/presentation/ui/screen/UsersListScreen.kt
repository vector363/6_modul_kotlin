package com.example.modul_6_kotlin.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.modul_6_kotlin.domain.model.User
import com.example.modul_6_kotlin.presentation.ui.component.UserItem
import com.example.modul_6_kotlin.presentation.viewmodel.UsersUiState
import com.example.modul_6_kotlin.presentation.viewmodel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListScreen(
    viewModel: UsersViewModel,
    onUserClick: (User) -> Unit,
    onLogout: () -> Unit
) {
    val usersState by viewModel.usersState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    IconButton(onClick = { viewModel.loadUsers() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Обновить"
                        )
                    }
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Выйти"
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
            when (usersState) {
                is UsersUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UsersUiState.Success -> {
                    val users = (usersState as UsersUiState.Success).users

                    if (users.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "👥 Нет пользователей",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadUsers() }) {
                                Text("Обновить")
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(
                                items = users,
                                key = { it.id }
                            ) { user ->
                                UserItem(
                                    user = user,
                                    onClick = { onUserClick(user) }
                                )
                            }
                        }
                    }
                }

                is UsersUiState.Error -> {
                    val errorMessage = (usersState as UsersUiState.Error).message
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
                        Button(onClick = { viewModel.loadUsers() }) {
                            Text("Повторить")
                        }
                    }
                }
            }
        }
    }
}