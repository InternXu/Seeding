package com.example.seeding.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * 表示用户的行为记录
 */
@Entity(
    tableName = "actions",
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
data class Action(
    @PrimaryKey val actionId: String = UUID.randomUUID().toString(),
    val userId: String,
    val title: String,
    val description: String = "",
    val seedIds: List<Int>, // 对应的种子ID列表
    val isPositive: Boolean, // 是否是正面行为
    val relatedGoalIds: List<String> = emptyList(), // 相关的目标ID列表，仅对正面行为有效
    val createdAt: Long = System.currentTimeMillis(),
    val promiseId: String? = null // 如果是负面行为，可能会创建一个关联的承诺
) 