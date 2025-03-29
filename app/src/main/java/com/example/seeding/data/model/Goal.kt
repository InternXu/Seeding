package com.example.seeding.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "goals",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class Goal(
    @PrimaryKey val goalId: String = UUID.randomUUID().toString(),
    val userId: String,
    val title: String,
    val description: String = "",
    val seedIds: List<Int>, // 对应的种子ID列表
    val deadline: Long, // 截止时间戳
    val createdAt: Long = System.currentTimeMillis(),
    val status: GoalStatus = GoalStatus.IN_PROGRESS,
    val progressMilestones: List<ProgressMilestone> = emptyList()
)

/**
 * 表示目标的进度里程碑
 */
data class ProgressMilestone(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val deadline: Long,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)

/**
 * 表示目标的状态
 */
enum class GoalStatus {
    IN_PROGRESS, // 进行中
    COMPLETED, // 已完成
    OVERDUE, // 已超时
    OVERDUE_COMPLETED, // 超时后完成
    ABANDONED // 已放弃
}

/**
 * 表示承诺（短期目标）
 */
@Entity(
    tableName = "promises",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class Promise(
    @PrimaryKey val promiseId: String = UUID.randomUUID().toString(),
    val userId: String,
    val title: String,
    val description: String = "",
    val seedId: Int, // 对应的种子ID
    val deadline: Long, // 截止时间戳，不得超过创建时间+12小时
    val createdAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val relatedNegativeActionId: String? = null // 关联的负面行为ID，如果有的话
) 