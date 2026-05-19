package com.example.weathersnap.data.local.repository

import com.example.weathersnap.data.local.dao.ReportDao
import com.example.weathersnap.data.local.entity.ReportEntity
import com.example.weathersnap.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportDao: ReportDao
) : ReportRepository {
    override fun getAllReports(): Flow<List<ReportEntity>> {
        return reportDao.getAllReports()
    }

    override suspend fun saveReport(report: ReportEntity) {
        reportDao.insertReport(report)
    }
}
