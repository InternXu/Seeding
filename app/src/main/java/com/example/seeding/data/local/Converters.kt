package com.example.seeding.data.local

import androidx.room.TypeConverter
import com.example.seeding.data.model.ActionType
import com.example.seeding.data.model.CommitmentStatus

/**
 * Room数据库类型转换器
 * 用于将自定义类型（如枚举）转换为数据库可存储的类型
 */
class Converters {
    /**
     * ActionType枚举转换为整数
     */
    @TypeConverter
    fun fromActionType(value: ActionType): Int {
        return when (value) {
            ActionType.POSITIVE -> 0
            ActionType.NEGATIVE -> 1
            ActionType.NEUTRAL -> 2
        }
    }

    /**
     * 整数转换为ActionType枚举
     */
    @TypeConverter
    fun toActionType(value: Int): ActionType {
        return when (value) {
            0 -> ActionType.POSITIVE
            1 -> ActionType.NEGATIVE
            else -> ActionType.NEUTRAL
        }
    }

    /**
     * CommitmentStatus枚举转换为整数
     */
    @TypeConverter
    fun fromCommitmentStatus(value: CommitmentStatus): Int {
        return when (value) {
            CommitmentStatus.PENDING -> 0
            CommitmentStatus.UNFULFILLED -> 1
            CommitmentStatus.FULFILLED -> 2
            CommitmentStatus.EXPIRED -> 3
        }
    }

    /**
     * 整数转换为CommitmentStatus枚举
     */
    @TypeConverter
    fun toCommitmentStatus(value: Int): CommitmentStatus {
        return when (value) {
            0 -> CommitmentStatus.PENDING
            1 -> CommitmentStatus.UNFULFILLED
            2 -> CommitmentStatus.FULFILLED
            3 -> CommitmentStatus.EXPIRED
            else -> CommitmentStatus.PENDING
        }
    }
    
    /**
     * 整数列表转换为字符串
     */
    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return value.joinToString(",")
    }
    
    /**
     * 字符串转换为整数列表
     */
    @TypeConverter
    fun toIntList(value: String): List<Int> {
        return if (value.isBlank()) {
            emptyList()
        } else {
            value.split(",").map { it.toInt() }
        }
    }
    
    /**
     * 字符串列表转换为字符串
     */
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }
    
    /**
     * 字符串转换为字符串列表
     */
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isBlank()) {
            emptyList()
        } else {
            value.split(",")
        }
    }
} 