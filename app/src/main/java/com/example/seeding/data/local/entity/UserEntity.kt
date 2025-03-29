package com.example.seeding.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.seeding.domain.model.User

/**
 * 用户实体 - 数据层（数据库）
 * 用于本地数据库存储的用户实体
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val phoneNumber: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: Long,
    val lastLoginAt: Long,
    val isCurrentUser: Boolean = false // 标记当前登录用户
) {
    /**
     * 将数据库实体转换为领域模型
     */
    fun toDomainModel(): User {
        return User(
            id = id,
            username = username,
            phoneNumber = phoneNumber,
            email = email,
            avatarUrl = avatarUrl,
            createdAt = createdAt,
            lastLoginAt = lastLoginAt
        )
    }
}

/**
 * 扩展函数，将领域模型转换为数据库实体
 */
fun User.toEntity(isCurrentUser: Boolean = false): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        phoneNumber = phoneNumber,
        email = email,
        avatarUrl = avatarUrl,
        createdAt = createdAt,
        lastLoginAt = lastLoginAt,
        isCurrentUser = isCurrentUser
    )
} 