package com.example.weatherapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.navigation.WeatherNavHost


// Top level composable that represents screens for the application.
@Composable
fun WeatherApp(navController: NavHostController = rememberNavController()) {
    WeatherNavHost(navController = navController)
}