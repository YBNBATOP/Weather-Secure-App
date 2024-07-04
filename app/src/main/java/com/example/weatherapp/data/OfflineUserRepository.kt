package com.example.weatherapp.data

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    override suspend fun getUser(username: String): User {
        return userDao.getUser(username)
    }

    override suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }
}