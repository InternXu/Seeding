package com.example.seeding.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.seeding.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * 日期工具类
 */
object DateUtils {
    // 日期格式化
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    
    /**
     * 格式化日期为标准格式
     */
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    /**
     * 格式化日期时间为标准格式
     */
    fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    /**
     * 格式化倒计时时间
     */
    fun formatCountdownTime(millisRemaining: Long): String {
        if (millisRemaining <= 0) return "00:00" // 已过期
        
        val hours = TimeUnit.MILLISECONDS.toHours(millisRemaining)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisRemaining) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisRemaining) % 60
        
        return when {
            hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
            else -> String.format("%02d:%02d", minutes, seconds)
        }
    }
    
    /**
     * 获取剩余时间的友好显示（非Composable环境使用）
     */
    fun getRemainingTime(deadline: Long): String {
        val now = System.currentTimeMillis()
        if (deadline <= now) return "已过期" // 已过期
        
        val remainingMillis = deadline - now
        val days = TimeUnit.MILLISECONDS.toDays(remainingMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(remainingMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60
        
        return when {
            days > 0 -> "${days}天${hours}小时"
            hours > 0 -> "${hours}小时${minutes}分钟"
            else -> "${minutes}分钟"
        }
    }
    
    /**
     * 获取剩余时间的友好显示（带Context版本）
     */
    fun getRemainingTime(deadline: Long, context: Context): String {
        val now = System.currentTimeMillis()
        if (deadline <= now) return context.getString(R.string.overdue)
        
        val remainingMillis = deadline - now
        val days = TimeUnit.MILLISECONDS.toDays(remainingMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(remainingMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60
        
        return when {
            days > 0 -> context.getString(R.string.days_hours_format, days, hours)
            hours > 0 -> context.getString(R.string.hours_minutes_format, hours, minutes)
            else -> context.getString(R.string.minutes_format, minutes)
        }
    }
    
    /**
     * 获取剩余时间的友好显示（Composable环境使用）
     */
    @Composable
    fun getRemainingTimeComposable(deadline: Long): String {
        val now = System.currentTimeMillis()
        if (deadline <= now) return stringResource(R.string.overdue)
        
        val remainingMillis = deadline - now
        val days = TimeUnit.MILLISECONDS.toDays(remainingMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(remainingMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60
        
        return when {
            days > 0 -> stringResource(R.string.days_hours_format, days, hours)
            hours > 0 -> stringResource(R.string.hours_minutes_format, hours, minutes)
            else -> stringResource(R.string.minutes_format, minutes)
        }
    }
    
    /**
     * 获取精确到秒的剩余时间友好显示（非Composable环境使用）
     */
    fun getRemainingTimeWithSeconds(currentTime: Long, deadline: Long): String {
        if (deadline <= currentTime) return "已过期" // 已过期
        
        val remainingMillis = deadline - currentTime
        val hours = TimeUnit.MILLISECONDS.toHours(remainingMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60
        
        return when {
            hours > 0 -> "${hours}小时${minutes}分钟${seconds}秒"
            minutes > 0 -> "${minutes}分钟${seconds}秒"
            else -> "${seconds}秒"
        }
    }
    
    /**
     * 获取精确到秒的剩余时间友好显示（带Context版本）
     */
    fun getRemainingTimeWithSeconds(currentTime: Long, deadline: Long, context: Context): String {
        if (deadline <= currentTime) return context.getString(R.string.overdue)
        
        val remainingMillis = deadline - currentTime
        val hours = TimeUnit.MILLISECONDS.toHours(remainingMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60
        
        return when {
            hours > 0 -> context.getString(R.string.hours_minutes_seconds_format, hours, minutes, seconds)
            minutes > 0 -> context.getString(R.string.minutes_seconds_format, minutes, seconds)
            else -> context.getString(R.string.seconds_format, seconds)
        }
    }
    
    /**
     * 获取精确到秒的剩余时间友好显示（Composable环境使用）
     */
    @Composable
    fun getRemainingTimeWithSecondsComposable(currentTime: Long, deadline: Long): String {
        if (deadline <= currentTime) return stringResource(R.string.overdue)
        
        val remainingMillis = deadline - currentTime
        val hours = TimeUnit.MILLISECONDS.toHours(remainingMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60
        
        return when {
            hours > 0 -> stringResource(R.string.hours_minutes_seconds_format, hours, minutes, seconds)
            minutes > 0 -> stringResource(R.string.minutes_seconds_format, minutes, seconds)
            else -> stringResource(R.string.seconds_format, seconds)
        }
    }
    
    /**
     * 判断是否已过期
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
    
    /**
     * 获取当前日期加上指定天数的时间戳
     */
    fun getDatePlusDays(days: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.timeInMillis
    }
} 