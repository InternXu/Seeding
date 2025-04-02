package com.example.seeding.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.seeding.data.model.ActionType

/**
 * 行为实体类 - 用于数据库存储
 */
@Entity(
    tableName = "actions",
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
data class ActionEntity(
    @PrimaryKey
    val actionId: String,
    val userId: String,             // 关联的用户ID
    val content: String,            // 行为内容描述
    val type: ActionType,           // 行为类型（正面/负面）
    val seedIds: String = "",       // 关联的种子IDs，以逗号分隔存储
    val goalIds: String = "",       // 关联的目标IDs，以逗号分隔存储
    val createdAt: Long = System.currentTimeMillis(), // 创建时间
    val hasCommitment: Boolean = false  // 是否有承诺
) 