package com.example.seeding.domain.repository

import com.example.seeding.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * 用户仓库接口 - 领域层
 * 定义用户相关的数据操作，不包含具体实现
 */
interface UserRepository {
    /**
     * 获取当前登录用户
     */
    fun getCurrentUser(): Flow<User?>
    
    /**
     * 设置当前用户
     */
    suspend fun setCurrentUser(userId: String)
    
    /**
     * 根据用户ID获取用户
     */
    suspend fun getUserById(userId: String): User?
    
    /**
     * 根据手机号获取用户
     */
    suspend fun getUserByPhoneNumber(phoneNumber: String): User?
    
    /**
     * 保存用户信息
     */
    suspend fun saveUser(user: User)
    
    /**
     * 使用手机号登录
     */
    suspend fun loginWithPhone(phoneNumber: String, password: String): Result<User>
} 