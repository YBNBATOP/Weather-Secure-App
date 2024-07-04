package com.example.weatherapp.ui.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.OfflineUserRepository
import com.example.weatherapp.data.User
import com.example.weatherapp.ui.helper.UserViewModel
import com.example.weatherapp.ui.helper.malware
import com.example.weatherapp.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.app_name
}

@Composable
fun LoginScreen(
    onSuccessfulLogin: () -> Unit,
    onRegistrationClick: () -> Unit,
    viewModel: UserViewModel,
    userRepository: OfflineUserRepository,
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var dialogState by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                malware(context, "login")
                coroutineScope.launch {
                    try {
                        userRepository.getUser(username)
                        val user = User(username = username, password = password, measurement = "")
                        if (checkLoginCredentials(
                                user,
                                userRepository.getUser(username),
                                viewModel
                            )
                        ) {
                            onSuccessfulLogin()
                        } else {
                            dialogState = true
                        }
                    } catch (e: NullPointerException) {
                        Log.e("LoginScreen", "Error: ${e.message}")
                        dialogState = true
                    }
                }
            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                malware(context, "login")
                coroutineScope.launch {
                    try {
                        userRepository.getUser(username)
                        val user = User(
                            username = username,
                            password = password,
                            measurement = "metric"//userRepository.getUser(username).measurement
                        )
                        if (checkLoginCredentials(
                                user,
                                userRepository.getUser(username),
                                viewModel
                            )
                        ) {
                            onSuccessfulLogin()
                        } else {
                            dialogState = true
                        }
                    } catch (e: NullPointerException) {
                        Log.e("LoginScreen", "Error: ${e.message}")
                        dialogState = true
                    }
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(stringResource(R.string.login_button), fontSize = 18.sp)
        }

        // Show dialog if login fails
        if (dialogState) {
            AlertDialog(
                onDismissRequest = { dialogState = false },
                title = { Text(stringResource(R.string.error_message_dialog)) },
                text = { Text(stringResource(R.string.invalid_user_credential_text)) },
                confirmButton = {
                    Button(
                        onClick = { dialogState = false }
                    ) {
                        Text(stringResource(R.string.accept_message))
                    }
                }
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onRegistrationClick()
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(stringResource(R.string.register_button), fontSize = 18.sp)
        }
    }
}

fun checkLoginCredentials(user: User, userToCheck: User, viewModel: UserViewModel): Boolean {
    Log.i("LoginScreen", "Checking login credentials")
    Log.i("Username:Password that got", "${user.username}:${user.password}")
    Log.i("Username:Password that should be", "${userToCheck.username}:${userToCheck.password}")
    if (user.username != userToCheck.username) {
        Log.i("LoginScreen", "Username does not match")
        return false
    } else if (user.password != userToCheck.password) {
        Log.i("LoginScreen", "Password does not match")
        return false
    } else {
        val userToUpdate = User(user.id, user.username, user.password, userToCheck.measurement) // adding this user variable to actually put the values in the viewmodel
        viewModel.updateUser(userToUpdate) // old one = viewModel.updateUser(user)
        Log.i("LoginScreen", "Login successful")
        return true
    }
}



