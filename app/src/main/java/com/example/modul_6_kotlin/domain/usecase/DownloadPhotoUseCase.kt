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
                println("📥 Загрузка фото: ${photo.id}")

                // 1. Загружаем изображение из интернета
                val url = URL(photo.downloadUrl)
                val inputStream = url.openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                if (bitmap == null) {
                    return@withContext Result.failure(Exception("Не удалось загрузить изображение"))
                }
                println("✅ Изображение загружено, размер: ${bitmap.width}x${bitmap.height}")

                // 2. Создаем запись в MediaStore.Downloads
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, "photo_${photo.id}.jpg")
                    put(MediaStore.Downloads.MIME_TYPE, "image/jpeg")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                        put(MediaStore.Downloads.IS_PENDING, 1)
                    }
                }

                // 3. Вставляем запись в MediaStore
                val uri = context.contentResolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                if (uri == null) {
                    println("❌ Не удалось создать запись в MediaStore")
                    return@withContext Result.failure(Exception("Не удалось создать запись"))
                }
                println("✅ Запись создана: $uri")

                // 4. Сохраняем изображение
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 95, outputStream)
                    println("✅ Изображение сохранено")
                } ?: run {
                    println("❌ Не удалось открыть поток для записи")
                    return@withContext Result.failure(Exception("Не удалось сохранить изображение"))
                }

                // 5. Завершаем транзакцию (только для Android 10+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                    context.contentResolver.update(uri, contentValues, null, null)
                    println("✅ Транзакция завершена")
                }

                println("✅ Фото успешно сохранено в папку Загрузки")
                Result.success(uri)

            } catch (e: Exception) {
                println("❌ Ошибка: ${e.message}")
                e.printStackTrace()
                Result.failure(Exception("Ошибка: ${e.message}"))
            }
        }
    }
}