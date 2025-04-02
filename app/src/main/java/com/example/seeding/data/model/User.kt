package com.example.seeding.data.model

/**
 * 用户领域模型类 - 用于UI层
 */
data class User(
    val userId: String,
    val username: String,
    val phoneNumber: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val createdAt: Long = 0L,
    val lastLoginAt: Long = 0L,
    val isCurrentUser: Boolean = false
)

/**
 * 会员类型枚举
 */
enum class MembershipType {
    FREE, // 免费用户
    PREMIUM // 付费用户
} 