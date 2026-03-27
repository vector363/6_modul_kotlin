package com.example.modul_6_kotlin.data.api

import com.example.modul_6_kotlin.data.model.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApiService {

    @GET("v2/list")
    suspend fun getPhotos(
        @Query("limit") limit: Int
    ): List<PhotoDto>
}