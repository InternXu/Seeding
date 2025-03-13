package com.example.seeding.data.remote.dto

/**
 * 行为请求DTO
 */
data class BehaviorRequest(
    val content: String,
    val seedIds: List<Int>,
    val isPositive: Boolean,
    val relatedTargets: List<BehaviorTargetRelationDto>? = null,
    val promiseRequest: TargetRequest? = null,
    val location: String? = null,
    val performedAt: String
)

/**
 * 行为响应DTO
 */
data class BehaviorResponse(
    val behaviorId: String,
    val content: String,
    val seedIds: List<Int>,
    val isPositive: Boolean,
    val relatedTargets: List<BehaviorTargetRelationDto>? = null,
    val promiseId: String? = null,
    val location: String? = null,
    val performedAt: String,
    val createdAt: String,
    val updatedAt: String
)

/**
 * 行为DTO
 */
data class BehaviorDto(
    val behaviorId: String,
    val content: String,
    val seedIds: List<Int>,
    val isPositive: Boolean,
    val relatedTargets: List<BehaviorTargetRelationDto>? = null,
    val promiseId: String? = null,
    val location: String? = null,
    val performedAt: String,
    val createdAt: String,
    val updatedAt: String
)

/**
 * 行为与目标关系DTO
 */
data class BehaviorTargetRelationDto(
    val targetId: String,
    val impact: String // PROMOTE, HINDER, NEUTRAL
) 