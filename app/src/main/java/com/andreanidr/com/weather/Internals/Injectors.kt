package com.andreanidr.com.weather.Internals

import android.content.Context
import com.andreanidr.com.weather.data.CurrentWeatherDao
import com.andreanidr.com.weather.data.Repository
import com.andreanidr.com.weather.data.RepositoryImpl
import com.andreanidr.com.weather.viewmodel.CurrentWeatherViewModelFactory

object Injectors {
    fun provideCurrentWeatherViewModelFactory(context: Context, appID: String) : CurrentWeatherViewModelFactory {
        val repository = RepositoryImpl(CurrentWeatherDao.getInstance(context, appID))
        return CurrentWeatherViewModelFactory(repository)
    }
}