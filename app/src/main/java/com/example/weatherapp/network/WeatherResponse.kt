package com.example.weatherapp.network

data class WeatherResponse(
    val coord: Coord,
    val main: Main,
    val name: String,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind
)

data class Main(
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class Sys(
    val country: String
)

data class Weather(
    val description: String,
    val icon: String,
    val main: String
)

data class Wind(
    val speed: Double
)