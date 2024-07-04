package com.example.weatherapp.ui.registration

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.OfflineUserRepository
import com.example.weatherapp.data.User
import com.example.weatherapp.ui.helper.UserViewModel
import com.example.weatherapp.ui.helper.malware
import com.example.weatherapp.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object RegistrationDestination : NavigationDestination {
    override val route = "registration"
    override val titleRes = R.string.registration
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onLoginClick: () -> Unit,
    userRepository: OfflineUserRepository,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedMeasurement by remember { mutableStateOf(Measurement.metric) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text(stringResource(R.string.username_text)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(stringResource(R.string.password_text)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                malware(context, "register")
                coroutineScope.launch {
                    val userEntity = User(username = username, password = password, measurement = selectedMeasurement.name)
                    userRepository.insertUser(userEntity)
                }
            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = selectedMeasurement.name,
                    onValueChange = { selectedMeasurement = Measurement.valueOf(it) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Measurement.entries.map { it.name }.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedMeasurement = Measurement.valueOf(item)
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                malware(context, "register")
                coroutineScope.launch {
                          val userEntity = User(username = username, password = password, measurement = selectedMeasurement.name)
                          userRepository.insertUser(userEntity)
                      }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(stringResource(R.string.register_button), fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onLoginClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(stringResource(R.string.login_button), fontSize = 18.sp)
        }
    }
}


enum class Measurement {
    metric,
    imperial
}

@Preview
@Composable
fun RegistrationScreenPreview() {
    //RegistrationScreen(onLoginClick = {})
}

