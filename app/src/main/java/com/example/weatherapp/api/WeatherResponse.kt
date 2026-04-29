package com.example.weatherapp.api

import com.google.gson.annotations.SerializedName

// Esta es la clase principal: representa toda la respuesta de la API
data class WeatherResponse(
    @SerializedName("name") val cityName: String,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("cod") val cod: Int
)

// Datos principales: temperatura, humedad, presión
data class Main(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("pressure") val pressure: Int
)

// Descripción del clima (puede haber varias, por eso es una lista en WeatherResponse)
data class Weather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

// Información del viento
data class Wind(
    @SerializedName("speed") val speed: Double
)

// Información del sistema (país de la ciudad)
data class Sys(
    @SerializedName("country") val country: String?
)