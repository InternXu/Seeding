package com.example.seeding.domain.repository

import com.example.seeding.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * 用户仓库接口 - 领域层
 * 定义用户相关的数据操作，不包含具体实现
 */
interface UserRepository {
    /**
     * 根据手机号码登录
     */
    suspend fun loginWithPhone(phoneNumber: String, password: String): Result<User>
    
    /**
     * 获取当前登录用户
     */
    fun getCurrentUser(): Flow<User?>
    
    /**
     * 使用手机号码和验证码注册新用户
     */
    suspend fun registerWithPhone(
        phoneNumber: String, 
        verificationCode: String, 
        password: String,
        username: String
    ): Result<User>
    
    /**
     * 发送验证码
     */
    suspend fun sendVerificationCode(phoneNumber: String): Result<Boolean>
    
    /**
     * 验证验证码
     */
    suspend fun verifyCode(phoneNumber: String, code: String): Result<Boolean>
    
    /**
     * 通过验证码重置密码
     */
    suspend fun resetPasswordWithCode(
        phoneNumber: String,
        verificationCode: String,
        newPassword: String
    ): Result<Boolean>
    
    /**
     * 登出
     */
    suspend fun logout(): Result<Boolean>
} 