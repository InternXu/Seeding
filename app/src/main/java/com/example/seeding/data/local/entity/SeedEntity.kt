package com.example.seeding.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 种子实体类 - 用于数据库存储
 */
@Entity(tableName = "seeds")
data class SeedEntity(
    @PrimaryKey
    val id: Int,
    val name: String,        // 种子简称
    val fullName: String,    // 种子全称
    val createdAt: Long = System.currentTimeMillis()
) 