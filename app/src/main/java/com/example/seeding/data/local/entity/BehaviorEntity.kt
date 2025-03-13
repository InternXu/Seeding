package com.example.seeding.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

/**
 * 行为实体类
 * 存储用户记录的行为信息
 */
@Entity(
    tableName = "behaviors",
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
data class BehaviorEntity(
    @PrimaryKey
    val behaviorId: String = UUID.randomUUID().toString(),
    val userId: String,
    val content: String,
    val seedIds: List<Int>,  // 关联的种子ID列表
    val isPositive: Boolean, // 是否为正面行为
    val relatedTargetIds: List<String>?, // 关联的目标ID列表
    val promiseId: String?, // 如果是负面行为，可能关联一个承诺
    val location: String?, // 可选的位置信息
    val performedAt: LocalDateTime, // 行为执行时间
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

/**
 * 行为与目标的关联实体
 * 用于存储行为与目标的多对多关系
 */
@Entity(
    tableName = "behavior_target_cross_ref",
    primaryKeys = ["behaviorId", "targetId"],
    foreignKeys = [
        ForeignKey(
            entity = BehaviorEntity::class,
            parentColumns = ["behaviorId"],
            childColumns = ["behaviorId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TargetEntity::class,
            parentColumns = ["targetId"],
            childColumns = ["targetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("behaviorId"),
        Index("targetId")
    ]
)
data class BehaviorTargetCrossRef(
    val behaviorId: String,
    val targetId: String,
    val impact: BehaviorImpact, // 行为对目标的影响
    val createdAt: LocalDateTime,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

/**
 * 行为对目标的影响枚举
 */
enum class BehaviorImpact {
    PROMOTE,    // 促进
    HINDER,     // 阻碍
    NEUTRAL     // 中性
} 