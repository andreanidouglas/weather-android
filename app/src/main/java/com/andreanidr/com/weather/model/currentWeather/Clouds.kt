package com.andreanidr.com.weather.model.currentWeather


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)