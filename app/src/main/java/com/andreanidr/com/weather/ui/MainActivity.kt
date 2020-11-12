package com.andreanidr.com.weather.ui


import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.andreanidr.com.weather.Internals.Injectors
import com.andreanidr.com.weather.R

import com.andreanidr.com.weather.model.CurrentWeather
import com.andreanidr.com.weather.viewmodel.CurrentWeatherViewModel
import com.andreanidr.com.weather.viewmodel.CurrentWeatherViewModelFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var currentWeatherViewModelFactory: CurrentWeatherViewModelFactory
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var mJob: Job

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        mJob = Job()

    }



    override fun onResume() {
        super.onResume()
        Log.d("OnResume", "onResume: called")

        currentWeatherViewModelFactory = Injectors.provideCurrentWeatherViewModelFactory(applicationContext, "")
        viewModel = ViewModelProvider(this, currentWeatherViewModelFactory).get(CurrentWeatherViewModel::class.java)

        bindUI()

    }

    private fun bindUI() = launch {
        val cityName = findViewById<TextView>(R.id.city)
        val temp = findViewById<TextView>(R.id.Temp)
        val d = viewModel.currentWeather
        val currentweather = viewModel.currentWeather
        currentweather.observe(this@MainActivity, Observer {
            if (it == null) return@Observer
            cityName.text = it.name
            temp.text = it.main.temp.toString()
        })
    }
}
