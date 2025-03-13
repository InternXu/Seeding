package com.example.seeding.domain.repository

import com.example.seeding.data.local.entity.UserEntity
import com.example.seeding.domain.model.Resource
import kotlinx.coroutines.flow.Flow

/**
 * 认证仓库接口
 */
interface AuthRepository {
    
    /**
     * 用户登录
     */
    suspend fun login(email: String, password: String): Resource<UserEntity>
    
    /**
     * 用户注册
     */
    suspend fun register(username: String, email: String, password: String): Resource<UserEntity>
    
    /**
     * 刷新令牌
     */
    suspend fun refreshToken(): Resource<String>
    
    /**
     * 获取当前用户
     */
    fun getCurrentUser(): Flow<UserEntity?>
    
    /**
     * 获取访问令牌
     */
    suspend fun getAccessToken(): String?
    
    /**
     * 用户登出
     */
    suspend fun logout()
    
    /**
     * 检查用户是否已登录
     */
    fun isLoggedIn(): Flow<Boolean>
} 