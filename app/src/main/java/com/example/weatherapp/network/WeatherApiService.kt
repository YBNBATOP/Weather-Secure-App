package com.example.weatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): WeatherResponse
}

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}