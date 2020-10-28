package com.andreanidr.com.weather.Model

class CityWeather {

    var city: String
        get() = this.city
        set(value) {
            this.city = value
        }

    var temperature: Double?
        get() = this.temperature
        set(value) {
            this.temperature = value
        }

    var status: String?
        get() = this.status
        set(value) {
            this.status = value
        }

    var description: String?
        get() = this.description
        set(value) {
            this.description = value
        }

}