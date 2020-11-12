package com.andreanidr.com.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreanidr.com.weather.data.Repository
import kotlinx.coroutines.Dispatchers

class CurrentWeatherViewModel(private val repository: Repository) : ViewModel() {

    val currentWeather = liveData() {
       repository.fetchWeatherForCity("London")
       emitSource( repository.currentWeather )
    }




}