package com.example.weatherapp.ui.weather

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.network.WeatherResponse
import com.example.weatherapp.ui.helper.UserViewModel
import com.example.weatherapp.ui.navigation.NavigationDestination

object WeatherMainDestination : NavigationDestination {
    override val route = "weather"
    override val titleRes = R.string.weather_main
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherMainScreen(
    weatherMainUiState: WeatherMainUiState,
    viewModel: WeatherMainViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    when (weatherMainUiState) {
        is WeatherMainUiState.Loading -> LoadingScreen()
        is WeatherMainUiState.Success -> SuccessScreen(
            weatherMainUiState.weatherResponse,
            viewModel,
            userViewModel
        )

        is WeatherMainUiState.Error -> ErrorScreen(viewModel, userViewModel)
    }
}


@Composable
fun LoadingScreen() {
    Text("Loading...")
}

@Composable
fun ErrorScreen(
    viewModel: WeatherMainViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text field for entering location search term
            OutlinedTextField(
                value = viewModel.searchTerm,
                onValueChange = { viewModel.searchTerm = it },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.enter_location_text)) }
            )


            // Search button
            Button(
                onClick = {
                    viewModel.sendMessageToLogger(viewModel.searchTerm, userViewModel.currentUser.username)
                    viewModel.measurement = userViewModel.currentUser.measurement
                    Log.i("WeatherMainScreen", "measurement: ${viewModel.measurement}")
                    Log.i("WeatherMainScreen", "searchTerm: ${userViewModel.currentUser.username}")
                    viewModel.onCityChanged(viewModel.searchTerm, viewModel.measurement)
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(stringResource(R.string.search_button))
            }
        }
        Text("OOps, it looks like the weather data is not available for this location. Please try another one.")
    }
}

@Composable
fun SuccessScreen(
    weatherResponse: WeatherResponse,
    viewModel: WeatherMainViewModel,
    userViewModel: UserViewModel,
    onSearchClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text field for entering location search term
            OutlinedTextField(
                value = viewModel.searchTerm,
                onValueChange = { viewModel.searchTerm = it },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.enter_location_text)) }
            )


            // Search button
            Button(
                onClick = {
                    viewModel.sendMessageToLogger(viewModel.searchTerm, userViewModel.currentUser.username)
                    viewModel.measurement = userViewModel.currentUser.measurement
                    Log.i("WeatherMainScreen", "measurement: ${viewModel.measurement}")
                    Log.i("WeatherMainScreen", "searchTerm: ${userViewModel.currentUser.username}")
                    viewModel.onCityChanged(viewModel.searchTerm, viewModel.measurement)
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(stringResource(R.string.search_button))
            }
        }
        Text(
            text = "${weatherResponse.name}, ${weatherResponse.sys.country}",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )

        AsyncImage(
            model = "https://openweathermap.org/img/wn/${weatherResponse.weather[0].icon}@4x.png",
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Temperature: ${weatherResponse.main.temp}°C",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "Feels like: ${weatherResponse.main.feels_like}°C",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Description: ${weatherResponse.weather[0].main}",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Humidity: ${weatherResponse.main.humidity}%",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Wind Speed: ${weatherResponse.wind.speed} km/h",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun WeatherMainScreenPreview() {
    //WeatherMainScreen()
}