package com.example.weathersnap.domain.repository

import com.example.weathersnap.data.local.entity.ReportEntity
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getAllReports(): Flow<List<ReportEntity>>
    suspend fun saveReport(report: ReportEntity)
}
