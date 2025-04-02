package com.example.seeding.data.local.converter

import androidx.room.TypeConverter
import com.example.seeding.data.model.GoalStatus

/**
 * Room数据库类型转换器
 */
class Converters {
    // 种子ID列表与字符串之间的转换
    @TypeConverter
    fun fromSeedIdList(seedIds: List<Int>?): String {
        return seedIds?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toSeedIdList(seedIdsString: String?): List<Int> {
        return if (seedIdsString.isNullOrEmpty()) {
            emptyList()
        } else {
            seedIdsString.split(",").mapNotNull { 
                try { 
                    it.trim().toInt() 
                } catch (e: NumberFormatException) { 
                    null 
                }
            }
        }
    }

    // GoalStatus枚举与整数之间的转换
    @TypeConverter
    fun fromGoalStatus(status: GoalStatus?): Int {
        return status?.ordinal ?: GoalStatus.IN_PROGRESS.ordinal
    }

    @TypeConverter
    fun toGoalStatus(value: Int?): GoalStatus {
        return when {
            value == null -> GoalStatus.IN_PROGRESS
            value in GoalStatus.values().indices -> GoalStatus.values()[value]
            else -> GoalStatus.IN_PROGRESS
        }
    }
} 