package com.example.modul_6_kotlin.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modul_6_kotlin.domain.model.NobelPrize
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.modul_6_kotlin.presentation.viewmodel.FavoriteActionState
import com.example.modul_6_kotlin.presentation.viewmodel.NobelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NobelDetailScreen(
    prize: NobelPrize?,
    viewModel: NobelViewModel,
    onBack: () -> Unit
) {
    val favoriteState by viewModel.favoriteState.collectAsStateWithLifecycle()
    val isFavorite = prize?.let { viewModel.isFavorite(it.id) } ?: false

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали премии") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    if (prize != null) {
                        IconButton(
                            onClick = { viewModel.toggleFavorite(prize) },
                            enabled = favoriteState !is FavoriteActionState.Loading
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                                tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (prize == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Премия не найдена")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
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
                                text = "${prize.awardYear} год",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Категория: ${prize.category.replaceFirstChar { it.uppercase() }}",
                                fontSize = 16.sp
                            )
                            prize.dateAwarded?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Дата вручения: $it",
                                    fontSize = 14.sp
                                )
                            }
                            prize.prizeAmount?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Сумма премии: $it SEK",
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "👥 Лауреаты",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(prize.laureates) { laureate ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = laureate.fullName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = laureate.portion?.let { "Доля: $it" } ?: "",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "${laureate.motivation}",
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )

                            if (laureate.birthPlace != "Не указано" || laureate.birthDate != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Место рождения: ${laureate.birthPlace}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                laureate.birthDate?.let {
                                    Text(
                                        text = "Дата рождения: $it",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }

                if (favoriteState is FavoriteActionState.Success) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                text = (favoriteState as FavoriteActionState.Success).message,
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                if (favoriteState is FavoriteActionState.Error) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = (favoriteState as FavoriteActionState.Error).message,
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}