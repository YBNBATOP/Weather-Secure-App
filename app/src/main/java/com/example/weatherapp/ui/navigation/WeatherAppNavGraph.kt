package com.example.weatherapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.weatherapp.data.OfflineUserRepository
import com.example.weatherapp.data.UserDatabase
import com.example.weatherapp.ui.login.LoginDestination
import com.example.weatherapp.ui.login.LoginScreen
import com.example.weatherapp.ui.registration.RegistrationDestination
import com.example.weatherapp.ui.registration.RegistrationScreen
import com.example.weatherapp.ui.helper.UserViewModel
import com.example.weatherapp.ui.weather.WeatherMainDestination
import com.example.weatherapp.ui.weather.WeatherMainScreen
import com.example.weatherapp.ui.weather.WeatherMainViewModel


@Composable
fun WeatherNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val userViewModel: UserViewModel = viewModel() // Have a viewModel to store the current user or something
    val db = UserDatabase.getDatabase(context = LocalContext.current) // Get a database
    val userRepository = OfflineUserRepository(userDao = db.userDao()) // Initialize the repository
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        composable(route = LoginDestination.route) {
            LoginScreen(
                onSuccessfulLogin = { navController.navigate(WeatherMainDestination.route) },
                onRegistrationClick = { navController.navigate(RegistrationDestination.route) },
                viewModel = userViewModel,
                userRepository = userRepository
            )
        }
        composable(
            route = WeatherMainDestination.route,
        ) {
            val weatherMainViewModel: WeatherMainViewModel = viewModel()
            WeatherMainScreen(
                weatherMainUiState = weatherMainViewModel.weatherMainUiState, // ??? Maybe can only give one argument, or better to leave as it is
                viewModel = weatherMainViewModel,
                userViewModel = userViewModel
            )
        }
        composable(
            route = RegistrationDestination.route,
        ) {
            RegistrationScreen(
                onLoginClick = { navController.navigate(LoginDestination.route) },
                userRepository = userRepository
            )
        }
    }

}
