package com.example.seeding.data.repository

import com.example.seeding.data.local.UserDao
import com.example.seeding.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    fun getUserById(userId: String): Flow<User?> = userDao.getUserById(userId)

    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    
    suspend fun updateSeedCoin(userId: String, amount: Int) = userDao.updateSeedCoin(userId, amount)
    
    suspend fun updateLastLogin(userId: String, timestamp: Long = System.currentTimeMillis()) {
        userDao.updateLastLogin(userId, timestamp)
    }
} 