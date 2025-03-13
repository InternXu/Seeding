package com.example.seeding.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 种子实体类
 * 存储系统预定义的"好种子"信息
 */
@Entity(tableName = "seeds")
data class SeedEntity(
    @PrimaryKey
    val seedId: Int,
    val name: String,
    val description: String,
    val iconResId: Int,
    val category: SeedCategory,
    val parentSeedId: Int? = null,
    val isActive: Boolean = true
)

/**
 * 种子类别枚举
 */
enum class SeedCategory {
    MORAL,          // 道德类
    RELATIONSHIP,   // 人际关系类
    SELF_GROWTH,    // 自我成长类
    HEALTH,         // 健康类
    ENVIRONMENT     // 环境类
} 