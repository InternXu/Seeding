package com.example.seeding.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime
import java.util.UUID

/**
 * 目标实体类
 * 存储用户设定的目标信息
 */
@Entity(
    tableName = "targets",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class TargetEntity(
    @PrimaryKey
    val targetId: String = UUID.randomUUID().toString(),
    val userId: String,
    val title: String,
    val description: String,
    val seedIds: List<Int>,  // 关联的种子ID列表
    val startDate: LocalDateTime,
    val dueDate: LocalDateTime,
    val originalDueDate: LocalDateTime,  // 原始截止日期，用于限制修改
    val status: TargetStatus,
    val isPromise: Boolean = false,  // 是否为"承诺"类型
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

/**
 * 目标进度实体类
 * 存储目标的进度点信息
 */
@Entity(
    tableName = "target_progress",
    foreignKeys = [
        ForeignKey(
            entity = TargetEntity::class,
            parentColumns = ["targetId"],
            childColumns = ["targetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("targetId")]
)
data class TargetProgressEntity(
    @PrimaryKey
    val progressId: String = UUID.randomUUID().toString(),
    val targetId: String,
    val title: String,
    val description: String?,
    val dueDate: LocalDateTime,
    val isCompleted: Boolean,
    val completedAt: LocalDateTime?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

/**
 * 目标状态枚举
 */
enum class TargetStatus {
    ACTIVE,         // 进行中
    COMPLETED,      // 已完成
    OVERDUE,        // 已逾期
    ABANDONED,      // 已放弃
    OVERDUE_COMPLETED  // 逾期完成
}

/**
 * 目标与进度的关系类
 * 用于Room的一对多关系查询
 */
data class TargetWithProgress(
    @Embedded val target: TargetEntity,
    @Relation(
        parentColumn = "targetId",
        entityColumn = "targetId"
    )
    val progressList: List<TargetProgressEntity>
) 