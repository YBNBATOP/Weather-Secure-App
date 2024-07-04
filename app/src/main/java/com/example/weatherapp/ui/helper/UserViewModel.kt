package com.example.weatherapp.ui.helper

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.OfflineUserRepository
import com.example.weatherapp.data.User

class UserViewModel() : ViewModel() {

    var currentUser: User = User(username = "", password = "", measurement = "metric") // measurement = "" was the old version
        private set

    fun updateUser(user: User) {
        currentUser = user
    }

    fun getUser(): User {
        return currentUser
    }
}