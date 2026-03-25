package com.example.modul_6_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.modul_6_kotlin.presentation.ui.BleScannerScreen
import com.example.modul_6_kotlin.ui.theme.Modul_6_KotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Modul_6_KotlinTheme {
                BleScannerScreen()
            }
        }
    }
}