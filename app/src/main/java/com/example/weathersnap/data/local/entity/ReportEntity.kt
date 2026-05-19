package com.example.weathersnap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val city: String,
    val temperature: Double,
    val humidity: Int,
    val pressure: Double,
    val windSpeed: Double,
    val condition: String,
    val notes: String,
    val imagePath: String,
    val originalImageSize: Long,
    val compressedImageSize: Long,
    val timestamp: Long
)
