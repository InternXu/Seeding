package com.example.seeding.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String,
    val username: String,
    val email: String,
    val avatarUrl: String? = null,
    val membershipType: MembershipType = MembershipType.FREE,
    val seedCoin: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
)

enum class MembershipType {
    FREE, // 免费用户
    EXPERIENCE, // 体验会员
    ANNUAL, // 年度会员
    PROFESSIONAL // 专业会员
} 