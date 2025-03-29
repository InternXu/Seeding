package com.example.seeding.data.remote.dto

import com.example.seeding.domain.model.User

/**
 * 用户DTO - 数据层（API响应）
 * 用于API请求和响应的数据传输对象
 */
data class UserDto(
    val userId: String,
    val userName: String,
    val phone: String,
    val emailAddress: String?,
    val profileImage: String?,
    val creationTimestamp: Long,
    val lastLoginTimestamp: Long
) {
    /**
     * 将DTO转换为领域模型
     */
    fun toDomainModel(): User {
        return User(
            id = userId,
            username = userName,
            phoneNumber = phone,
            email = emailAddress ?: "",
            avatarUrl = profileImage,
            createdAt = creationTimestamp,
            lastLoginAt = lastLoginTimestamp
        )
    }
}

/**
 * 扩展函数，将领域模型转换为DTO
 */
fun User.toDto(): UserDto {
    return UserDto(
        userId = id,
        userName = username,
        phone = phoneNumber,
        emailAddress = email,
        profileImage = avatarUrl,
        creationTimestamp = createdAt,
        lastLoginTimestamp = lastLoginAt
    )
} 