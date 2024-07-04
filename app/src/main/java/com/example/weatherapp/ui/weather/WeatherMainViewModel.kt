package com.example.weatherapp.ui.weather

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.network.LoggerApi
import com.example.weatherapp.network.LoggerRequest
import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.network.WeatherResponse
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface WeatherMainUiState {
    object Loading : WeatherMainUiState
    data class Success(val weatherResponse: WeatherResponse) : WeatherMainUiState
    object Error: WeatherMainUiState
}

class WeatherMainViewModel : ViewModel() {
    var weatherMainUiState: WeatherMainUiState by mutableStateOf(WeatherMainUiState.Loading)
        private set

    var city by mutableStateOf("Brugge")
    var measurement by mutableStateOf("metric")
    var searchTerm by mutableStateOf("")

    init {
        getWeather()
    }

    fun onCityChanged(newCity: String, newMeasurement: String) {
        city = newCity
        measurement = newMeasurement
        getWeather()
    }

    fun sendMessageToLogger(city: String, user: String) {
        viewModelScope.launch {
            try {
                val loggerResponse = LoggerApi.retrofitService.sendLog(
                    LoggerRequest("${user} - searched for - ${city}")
                )
                Log.d("WEATHER_LOGGER", "Message sent to logger API: ${loggerResponse.receivedMessage}")
            } catch (e: IOException) {
                // Do nothing
                Log.e("WEATHER_LOGGER_ERROR", "Could not send message to logger API: ${e.message}" )
            }
        }
    }

    private fun getWeather() {
        viewModelScope.launch {
            weatherMainUiState = WeatherMainUiState.Loading
            weatherMainUiState = try {
                val weatherResponse = WeatherApi.retrofitService.getWeather(
                    city,
                    measurement,
                    "<key here>"
                )
                WeatherMainUiState.Success(weatherResponse)
            } catch (e: Exception) {
                WeatherMainUiState.Error
            }
        }
    }
}