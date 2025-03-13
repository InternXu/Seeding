package com.example.seeding.data.local.util

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

/**
 * Room数据库类型转换器
 * 用于在数据库和应用程序之间转换复杂类型
 */
class Converters {
    
    /**
     * 将时间戳转换为Date对象
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    
    /**
     * 将Date对象转换为时间戳
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    /**
     * 将时间戳转换为LocalDateTime对象
     */
    @TypeConverter
    fun fromTimestampToLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        }
    }
    
    /**
     * 将LocalDateTime对象转换为时间戳
     */
    @TypeConverter
    fun localDateTimeToTimestamp(dateTime: LocalDateTime?): Long? {
        return dateTime?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }
    
    /**
     * 将逗号分隔的字符串转换为整数列表
     */
    @TypeConverter
    fun fromString(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }
    
    /**
     * 将整数列表转换为逗号分隔的字符串
     */
    @TypeConverter
    fun fromList(list: List<Int>?): String? {
        return list?.joinToString(",")
    }
    
    /**
     * 将逗号分隔的字符串转换为字符串列表
     */
    @TypeConverter
    fun fromStringToStringList(value: String?): List<String>? {
        return value?.split(",")
    }
    
    /**
     * 将字符串列表转换为逗号分隔的字符串
     */
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }
} 