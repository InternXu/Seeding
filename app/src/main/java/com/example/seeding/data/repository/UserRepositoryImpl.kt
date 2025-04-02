package com.example.seeding.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.seeding.data.local.dao.UserDao
import com.example.seeding.data.util.EntityMappers
import com.example.seeding.domain.model.User
import com.example.seeding.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用户仓库实现类
 * 临时实现，用于连接前端与后端API
 * 当前版本仅提供内存缓存实现，未连接实际后端
 */

// 为Context类扩展dataStore属性
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
private val CURRENT_USER_ID_KEY = stringPreferencesKey("current_user_id")

@Singleton
class UserRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userDao: UserDao
) : UserRepository {
    
    override fun getCurrentUser(): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            try {
                val currentUserId = preferences[CURRENT_USER_ID_KEY]
                if (!currentUserId.isNullOrEmpty()) {
                    val userEntity = userDao.getUserById(currentUserId)
                    userEntity?.let { EntityMappers.mapToUser(it) }
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
    
    override suspend fun setCurrentUser(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_USER_ID_KEY] = userId
        }
        
        // 更新用户的最后登录时间
        userDao.updateLastLogin(userId, System.currentTimeMillis())
    }
    
    override suspend fun getUserById(userId: String): User? {
        val userEntity = userDao.getUserById(userId) ?: return null
        return EntityMappers.mapToUser(userEntity)
    }
    
    override suspend fun getUserByPhoneNumber(phoneNumber: String): User? {
        val userEntity = userDao.getUserByPhone(phoneNumber) ?: return null
        return EntityMappers.mapToUser(userEntity)
    }
    
    override suspend fun saveUser(user: User) {
        val userEntity = EntityMappers.mapToUserEntity(user)
        userDao.insertUser(userEntity)
    }
    
    override suspend fun loginWithPhone(phoneNumber: String, password: String): Result<User> {
        // 简化的登录流程，实际应用中应该有密码验证
        val userEntity = userDao.getUserByPhone(phoneNumber) ?: return Result.failure(
            Exception("用户不存在")
        )
        
        // TODO: 实际应用中需要验证密码
        
        val user = EntityMappers.mapToUser(userEntity)
        setCurrentUser(user.id)
        return Result.success(user)
    }
} 