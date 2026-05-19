package com.example.weathersnap.di

import com.example.weathersnap.data.local.repository.ReportRepositoryImpl
import com.example.weathersnap.data.remote.repository.WeatherRepositoryImpl
import com.example.weathersnap.domain.repository.ReportRepository
import com.example.weathersnap.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl
    ): ReportRepository
}
