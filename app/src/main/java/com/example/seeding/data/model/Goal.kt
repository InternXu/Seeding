package com.example.seeding.data.model

/**
 * 目标领域模型类 - 用于UI层
 */
data class Goal(
    val goalId: String,
    val userId: String,
    val title: String,
    val description: String = "",
    val seedIds: List<Int>, // 关联的种子ID列表
    val deadline: Long, // 截止日期
    val createdAt: Long = System.currentTimeMillis(), // 创建时间
    val status: GoalStatus = GoalStatus.IN_PROGRESS
)

/**
 * 目标状态枚举
 */
enum class GoalStatus {
    IN_PROGRESS, // 进行中
    COMPLETED, // 已完成
    OVERDUE, // 已逾期
    OVERDUE_COMPLETED, // 逾期后完成
    ABANDONED // 已删除/放弃
} 