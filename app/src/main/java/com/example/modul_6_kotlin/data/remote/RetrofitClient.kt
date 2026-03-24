package com.example.modul_6_kotlin.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.modul_6_kotlin.data.api.PhotoApiService
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://picsum.photos/"

    /**
     * Интерсептор для логирования запросов и ответов
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY  // Логируем тело запроса и ответа
    }

    /**
     * OkHttp клиент с настройками
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)   // Таймаут подключения
        .readTimeout(30, TimeUnit.SECONDS)      // Таймаут чтения
        .writeTimeout(30, TimeUnit.SECONDS)     // Таймаут записи
        .build()

    /**
     * Экземпляр Retrofit
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Экземпляр API сервиса
     */
    val apiService: PhotoApiService = retrofit.create(PhotoApiService::class.java)
}