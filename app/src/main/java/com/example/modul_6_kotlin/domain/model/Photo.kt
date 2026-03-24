package com.example.modul_6_kotlin.domain.model

data class Photo(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val downloadUrl: String
) {
    val dimensions: String
        get() = "$width × $height"

    val thumbnailUrl: String
        get() = downloadUrl.replace("$width/$height", "200/200")

    val largeUrl: String
        get() = downloadUrl.replace("$width/$height", "800/800")
}