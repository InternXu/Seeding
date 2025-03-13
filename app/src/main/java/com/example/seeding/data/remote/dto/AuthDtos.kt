package com.example.seeding.data.remote.dto

/**
 * 认证请求DTO
 */
data class AuthRequest(
    val email: String,
    val password: String
)

/**
 * 注册请求DTO
 */
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

/**
 * 认证响应DTO
 */
data class AuthResponse(
    val userId: String,
    val username: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val membershipType: String,
    val seedCoins: Int
)

/**
 * 用户DTO
 */
data class UserDto(
    val userId: String,
    val username: String,
    val email: String,
    val membershipType: String,
    val seedCoins: Int,
    val createdAt: String,
    val lastLoginAt: String,
    val profileImageUrl: String?
) 