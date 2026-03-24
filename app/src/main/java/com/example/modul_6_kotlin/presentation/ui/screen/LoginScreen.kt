package com.example.modul_6_kotlin.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.modul_6_kotlin.presentation.viewmodel.LoginUiState
import com.example.modul_6_kotlin.presentation.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Авторизация") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Вход в систему",
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Поле ввода логина
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Логин") },
                        placeholder = { Text("emilys") },
                        singleLine = true,
                        isError = uiState is LoginUiState.Error
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Поле ввода пароля
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Пароль") },
                        placeholder = { Text("emilyspass") },
                        singleLine = true,
                        visualTransformation = if (showPassword) {
                            PasswordVisualTransformation()
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) {
                                        Icons.Default.Face
                                    } else {
                                        Icons.Default.Close
                                    },
                                    contentDescription = if (showPassword) "Скрыть пароль" else "Показать пароль"
                                )
                            }
                        },
                        isError = uiState is LoginUiState.Error
                    )

                    // Сообщение об ошибке
                    if (uiState is LoginUiState.Error) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = (uiState as LoginUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Кнопка входа
                    Button(
                        onClick = { viewModel.login(username, password) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = username.isNotBlank() && password.isNotBlank() && uiState !is LoginUiState.Loading
                    ) {
                        if (uiState is LoginUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Вход...")
                        } else {
                            Text("Войти")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Тестовые данные: emilys / emilyspass",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}