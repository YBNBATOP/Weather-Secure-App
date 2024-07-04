package com.example.weatherapp.data

interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun getUser(username: String): User
    suspend fun getAllUsers(): List<User>
}