package com.example.seeding.domain.model

/**
 * 用户实体 - 领域层
 * 这是应用程序的核心用户模型，与数据库实体和API响应分开
 */
data class User(
    val id: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val createdAt: Long = 0,
    val lastLoginAt: Long = 0
) 