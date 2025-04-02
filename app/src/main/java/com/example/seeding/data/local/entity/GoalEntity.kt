package com.example.seeding.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.seeding.data.model.GoalStatus

/**
 * 目标实体类 - 用于数据库存储
 */
@Entity(
    tableName = "goals",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class GoalEntity(
    @PrimaryKey
    val goalId: String,
    val userId: String,             // 关联的用户ID
    val title: String,              // 目标标题
    val description: String = "",   // 目标描述
    val seedIds: String = "",       // 关联的种子IDs，以逗号分隔存储
    val deadline: Long,             // 截止日期
    val createdAt: Long = System.currentTimeMillis(), // 创建时间
    val status: GoalStatus = GoalStatus.IN_PROGRESS,  // 目标状态
    val lastUpdated: Long = System.currentTimeMillis(),// 最后更新时间
    val isSynced: Boolean = false   // 是否已与云端同步
) 