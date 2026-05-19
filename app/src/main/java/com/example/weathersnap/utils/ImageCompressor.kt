package com.example.weathersnap.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext

class ImageCompressor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun compressImage(imagePath: String): File? {
        val file = File(imagePath)
        if (!file.exists()) return null

        val bitmap = BitmapFactory.decodeFile(imagePath) ?: return null
        val outputStream = ByteArrayOutputStream()
        
        // Quality 60%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
        
        val compressedFile = File(context.cacheDir, "compressed_${UUID.randomUUID()}.jpg")
        return try {
            val fos = FileOutputStream(compressedFile)
            fos.write(outputStream.toByteArray())
            fos.flush()
            fos.close()
            bitmap.recycle() // Production best practice: free up memory
            compressedFile
        } catch (e: Exception) {
            bitmap.recycle()
            null
        }
    }
}
