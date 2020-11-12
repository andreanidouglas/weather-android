package com.andreanidr.com.weather.data

import androidx.lifecycle.LiveData
import com.andreanidr.com.weather.model.CurrentWeather


interface Repository {
    val currentWeather: LiveData<CurrentWeather>
    suspend fun fetchWeatherForCity(city: String)
}