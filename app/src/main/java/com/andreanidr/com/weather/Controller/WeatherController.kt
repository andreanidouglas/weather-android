package com.andreanidr.com.weather.Controller

import com.andreanidr.com.weather.Model.CityWeather
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


class WeatherController {

    private val citiesWeather: HashMap<String, CityWeather> = HashMap<String, CityWeather>()
    private val urlBase = "https://api.openweathermap.org/data/2.5/weather?"

    fun addCity(city: String) {
        val w = CityWeather()
        w.city = city
        citiesWeather[city] = w
    }

    fun getCityByName(city: String): CityWeather? {
        return citiesWeather[city]
    }

    fun requestUpdateCityWeather(city: String): Request<JSONObject>? {
        val cw = citiesWeather[city]
        val url = "%sq=%s".format(urlBase, cw?.city)
        val req: JsonObjectRequest = JsonObjectRequest(

                Request.Method.GET, url, null,
                Response.Listener { response ->
                    run {
                        cw?.city = response.getString("name")
                        cw?.temperature =
                                response.getJSONObject("main").getDouble("temp")
                        cw?.status = (response.getJSONArray("weather")[0] as JSONObject).getString("main")
                        cw?.description = (response.getJSONArray("weather")[0] as JSONObject).getString("description")
                    }

                },
                Response.ErrorListener { error ->
                    run {
                        throw InvalidResponseException("got network response: %s".format(error.networkResponse))
                    }
                })
        if (cw != null) {
            return req
        }
        return null
    }
}

class InvalidResponseException(msg: String) : Exception() {

    private val msgI = msg
    override val message: String?
        get() = this.msgI
}