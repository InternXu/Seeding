package com.example.seeding.data.remote.api

import com.example.seeding.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 用户API接口 - 数据层（远程API）
 * 定义与用户相关的网络请求
 */
interface UserApi {
    /**
     * 登录请求
     */
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<ApiResponse<UserDto>>
    
    /**
     * 注册请求
     */
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<ApiResponse<UserDto>>
    
    /**
     * 发送验证码
     */
    @POST("auth/verification-code")
    suspend fun sendVerificationCode(@Body request: VerificationCodeRequest): Response<ApiResponse<Boolean>>
    
    /**
     * 验证验证码
     */
    @POST("auth/verify-code")
    suspend fun verifyCode(@Body request: VerifyCodeRequest): Response<ApiResponse<Boolean>>
    
    /**
     * 重置密码
     */
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ApiResponse<Boolean>>
    
    /**
     * 登出
     */
    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse<Boolean>>
    
    /**
     * 获取用户信息
     */
    @GET("users/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: String): Response<ApiResponse<UserDto>>
}

/**
 * 登录请求
 */
data class LoginRequest(
    val phoneNumber: String,
    val password: String
)

/**
 * 注册请求
 */
data class RegisterRequest(
    val phoneNumber: String,
    val verificationCode: String,
    val password: String,
    val username: String
)

/**
 * 验证码请求
 */
data class VerificationCodeRequest(
    val phoneNumber: String,
    val purpose: String // 目的：register, reset_password
)

/**
 * 验证验证码请求
 */
data class VerifyCodeRequest(
    val phoneNumber: String,
    val code: String
)

/**
 * 重置密码请求
 */
data class ResetPasswordRequest(
    val phoneNumber: String,
    val verificationCode: String,
    val newPassword: String
)

/**
 * API响应包装类
 */
data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String,
    val code: Int
) 