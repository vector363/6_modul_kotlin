package com.example.modul_6_kotlin.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.modul_6_kotlin.domain.model.NobelPrize
import com.example.modul_6_kotlin.presentation.ui.component.NobelItem
import com.example.modul_6_kotlin.presentation.viewmodel.NobelUiState
import com.example.modul_6_kotlin.presentation.viewmodel.NobelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NobelListScreen(
    viewModel: NobelViewModel,
    onPrizeClick: (NobelPrize) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Нобелевские лауреаты") },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Фильтр"
                        )
                    }
                    IconButton(onClick = { viewModel.loadPrizes() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Обновить"
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
            when (uiState) {
                is NobelUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is NobelUiState.Success -> {
                    val prizes = (uiState as NobelUiState.Success).prizes

                    if (prizes.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Нет данных",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Попробуйте изменить фильтры",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.clearFilters() }) {
                                Text("Сбросить фильтры")
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(
                                items = prizes,
                                key = { "${it.awardYear}_${it.category}" }
                            ) { prize ->
                                NobelItem(
                                    prize = prize,
                                    onClick = { onPrizeClick(prize) }
                                )
                            }
                        }
                    }
                }

                is NobelUiState.Error -> {
                    val errorMessage = (uiState as NobelUiState.Error).message
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
                        Button(onClick = { viewModel.loadPrizes() }) {
                            Text("Повторить")
                        }
                    }
                }
            }
        }
    }

    if (showFilterDialog) {
        FilterDialog(
            currentYear = selectedYear,
            currentCategory = selectedCategory,
            onYearChange = { year ->
                selectedYear = year
                viewModel.filterByYear(year)
            },
            onCategoryChange = { category ->
                selectedCategory = category
                viewModel.filterByCategory(category)
            },
            onClear = {
                selectedYear = null
                selectedCategory = null
                viewModel.clearFilters()
            },
            onDismiss = { showFilterDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    currentYear: String?,
    currentCategory: String?,
    onYearChange: (String?) -> Unit,
    onCategoryChange: (String?) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    val years = (1901..2024).map { it.toString() }.reversed()
    val categories = listOf(
        "chemistry" to "Химия",
        "physics" to "Физика",
        "medicine" to "Медицина",
        "literature" to "Литература",
        "peace" to "Мир",
        "economics" to "Экономика"
    )

    var yearExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Фильтры") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ExposedDropdownMenuBox(
                    expanded = yearExpanded,
                    onExpandedChange = { yearExpanded = it }
                ) {
                    OutlinedTextField(
                        value = currentYear ?: "Все годы",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = yearExpanded,
                        onDismissRequest = { yearExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все годы") },
                            onClick = {
                                onYearChange(null)
                                yearExpanded = false
                            }
                        )
                        years.forEach { year ->
                            DropdownMenuItem(
                                text = { Text(year) },
                                onClick = {
                                    onYearChange(year)
                                    yearExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = categories.find { it.first == currentCategory }?.second ?: "Все категории",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все категории") },
                            onClick = {
                                onCategoryChange(null)
                                categoryExpanded = false
                            }
                        )
                        categories.forEach { (code, name) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    onCategoryChange(code)
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Применить")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onClear()
                onDismiss()
            }) {
                Text("Сбросить")
            }
        }
    )
}