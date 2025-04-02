package com.example.seeding.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.seeding.data.model.CommitmentStatus

/**
 * 承诺实体类 - 用于数据库存储
 */
@Entity(
    tableName = "commitments",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActionEntity::class,
            parentColumns = ["actionId"],
            childColumns = ["actionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("actionId")]
)
data class CommitmentEntity(
    @PrimaryKey
    val commitmentId: String,
    val actionId: String,           // 关联的行为ID
    val userId: String,             // 关联的用户ID
    val content: String,            // 承诺内容
    val seedIds: String = "",       // 关联的种子IDs，以逗号分隔存储
    val timeFrame: Int,             // 时间框架（分钟）
    val createdAt: Long = System.currentTimeMillis(), // 创建时间
    val deadline: Long,             // 截止时间 = 创建时间 + 时间框架（分钟）* 60 * 1000
    val status: CommitmentStatus = CommitmentStatus.PENDING // 承诺状态
) 