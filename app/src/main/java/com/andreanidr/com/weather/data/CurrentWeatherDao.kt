package com.andreanidr.com.weather.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andreanidr.com.weather.model.CurrentWeather
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.io.IOException

class CurrentWeatherDao (
    context: Context,
    private val appid: String
) {

    private val currentWeather = MutableLiveData<CurrentWeather>()
    private val appContext = context.applicationContext


    fun getCurrentWeather() = currentWeather as LiveData<CurrentWeather>

    fun fetch (city: String) {
        var jsonText: String = ""
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$appid&units=metric"
        val queue = Volley.newRequestQueue(appContext)
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            val gson = Gson()
            jsonText = response
            val cw: CurrentWeather = gson.fromJson(jsonText, CurrentWeather::class.java)
            Log.d("onFetch repository", "$cw")
            currentWeather.postValue(cw)
        },
        Response.ErrorListener { throw IOException() })

        queue.add(stringRequest)

    }


    companion object {
        @Volatile
        private var instance: CurrentWeatherDao? = null
        fun getInstance(context: Context, appid: String) = instance ?: synchronized(this) {
            instance ?: CurrentWeatherDao(context, appid).also { instance = it }
        }
    }
}