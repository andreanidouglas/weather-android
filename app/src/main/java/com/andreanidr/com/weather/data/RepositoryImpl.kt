package com.andreanidr.com.weather.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.andreanidr.com.weather.model.CurrentWeather

class RepositoryImpl(private val currentWeatherDao: CurrentWeatherDao) : Repository {

    override val currentWeather = currentWeatherDao.getCurrentWeather()

    override suspend fun fetchWeatherForCity(city: String) {
        fetch(city)
    }

    private fun fetch(city: String) {
        currentWeatherDao.fetch(city)
    }

}