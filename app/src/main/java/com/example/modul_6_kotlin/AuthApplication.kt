package com.example.modul_6_kotlin

import android.app.Application
import com.example.modul_6_kotlin.data.TokenManager

class AuthApplication : Application() {

    lateinit var tokenManager: TokenManager

    override fun onCreate() {
        super.onCreate()
        tokenManager = TokenManager(this)
    }
}