package com.example.seeding.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户实体类 - 用于数据库存储
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val username: String,           // 用户名
    val phone: String? = null,      // 手机号
    val email: String? = null,      // 邮箱
    val passwordHash: String? = null, // 密码哈希
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long? = null
) 