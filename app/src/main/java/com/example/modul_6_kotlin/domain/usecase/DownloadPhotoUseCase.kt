package com.example.modul_6_kotlin.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.modul_6_kotlin.domain.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class DownloadPhotoUseCase {

    suspend fun downloadToMediaStore(context: Context, photo: Photo): Result<Uri> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(photo.downloadUrl)
                val inputStream = url.openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                if (bitmap == null) {
                    return@withContext Result.failure(Exception("Не удалось загрузить изображение"))
                }

                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, "photo_${photo.id}.jpg")
                    put(MediaStore.Downloads.MIME_TYPE, "image/jpeg")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                        put(MediaStore.Downloads.IS_PENDING, 1)
                    }
                }

                val uri = context.contentResolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                if (uri == null) {
                    return@withContext Result.failure(Exception("Не удалось создать запись"))
                }

                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 95, outputStream)
                } ?: run {
                    return@withContext Result.failure(Exception("Не удалось сохранить изображение"))
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                    context.contentResolver.update(uri, contentValues, null, null)
                }

                Result.success(uri)

            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(Exception("Ошибка: ${e.message}"))
            }
        }
    }
}