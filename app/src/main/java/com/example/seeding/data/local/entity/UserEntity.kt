package com.example.seeding.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * 用户实体类
 * 存储用户基本信息
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val username: String,
    val email: String,
    val passwordHash: String,
    val membershipType: MembershipType,
    val seedCoins: Int,
    val createdAt: LocalDateTime,
    val lastLoginAt: LocalDateTime,
    val profileImageUrl: String?,
    val isEmailVerified: Boolean,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

/**
 * 会员类型枚举
 */
enum class MembershipType {
    FREE,           // 免费用户
    TRIAL,          // 试用会员
    ANNUAL,         // 年度会员
    PROFESSIONAL    // 专业会员
}

/**
 * 同步状态枚举
 */
enum class SyncStatus {
    SYNCED,     // 已同步
    PENDING,    // 待同步
    FAILED      // 同步失败
} 