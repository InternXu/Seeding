package com.example.seeding.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {
    // 日期格式化
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    
    /**
     * 格式化日期为字符串(yyyy-MM-dd)
     */
    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }
    
    /**
     * 格式化日期时间为字符串(yyyy-MM-dd HH:mm)
     */
    fun formatDateTime(timestamp: Long): String {
        return dateTimeFormat.format(Date(timestamp))
    }
    
    /**
     * 计算两个时间戳之间的时间差，格式化为易读的字符串
     * 如：2天3小时、5小时30分钟
     */
    fun formatTimeDifference(startTime: Long, endTime: Long): String {
        val diff = endTime - startTime
        
        if (diff <= 0) return "0分钟"
        
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
        
        return when {
            days > 0 -> {
                if (hours > 0) "$days 天 $hours 小时" else "$days 天"
            }
            hours > 0 -> {
                if (minutes > 0) "$hours 小时 $minutes 分钟" else "$hours 小时"
            }
            else -> "$minutes 分钟"
        }
    }
    
    /**
     * 计算截止日期剩余时间
     */
    fun getRemainingTime(deadline: Long): String {
        val currentTime = System.currentTimeMillis()
        return if (deadline > currentTime) {
            formatTimeDifference(currentTime, deadline)
        } else {
            "已逾期"
        }
    }
    
    /**
     * 检查是否已经过期
     */
    fun isOverdue(deadline: Long): Boolean {
        return System.currentTimeMillis() > deadline
    }
    
    /**
     * 获取今日开始时间
     */
    fun getTodayStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    /**
     * 获取今日结束时间
     */
    fun getTodayEnd(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
    
    /**
     * 将日期字符串转换为时间戳
     */
    fun parseDate(dateString: String): Long {
        return try {
            dateFormat.parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
} 