package com.example.seeding.data.remote.dto

/**
 * 目标请求DTO
 */
data class TargetRequest(
    val title: String,
    val description: String,
    val seedIds: List<Int>,
    val startDate: String,
    val dueDate: String,
    val isPromise: Boolean = false,
    val progressPoints: List<TargetProgressDto>? = null
)

/**
 * 目标响应DTO
 */
data class TargetResponse(
    val targetId: String,
    val title: String,
    val description: String,
    val seedIds: List<Int>,
    val startDate: String,
    val dueDate: String,
    val originalDueDate: String,
    val status: String,
    val isPromise: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val progressPoints: List<TargetProgressDto>? = null
)

/**
 * 目标DTO
 */
data class TargetDto(
    val targetId: String,
    val title: String,
    val description: String,
    val seedIds: List<Int>,
    val startDate: String,
    val dueDate: String,
    val originalDueDate: String,
    val status: String,
    val isPromise: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val progressPoints: List<TargetProgressDto>? = null
)

/**
 * 目标进度DTO
 */
data class TargetProgressDto(
    val progressId: String? = null,
    val title: String,
    val description: String? = null,
    val dueDate: String,
    val isCompleted: Boolean = false,
    val completedAt: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) 