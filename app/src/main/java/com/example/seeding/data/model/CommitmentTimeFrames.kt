package com.example.seeding.data.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.seeding.R
import com.example.seeding.util.LanguageManager
import java.util.Locale
import android.content.Context

/**
 * Commitment time frames constants (unit: minutes)
 */
object CommitmentTimeFrames {
    const val THREE_MINUTES = 3
    const val FIFTEEN_MINUTES = 15
    const val THIRTY_MINUTES = 30
    const val ONE_HOUR = 60
    const val TWO_HOURS = 120
    const val THREE_HOURS = 180
    
    val OPTIONS = listOf(
        THREE_MINUTES,
        FIFTEEN_MINUTES,
        THIRTY_MINUTES,
        ONE_HOUR,
        TWO_HOURS,
        THREE_HOURS
    )
    
    // 获取时间显示文本 - 根据当前应用语言环境
    fun getDisplayText(minutes: Int): String {
        // 这个方法已废弃，应该使用带Context的方法或者Composable方法
        // 但为了兼容保留，返回简单的英文格式
        return when (minutes) {
            THREE_MINUTES -> "3 min"
            FIFTEEN_MINUTES -> "15 min"
            THIRTY_MINUTES -> "30 min"
            ONE_HOUR -> "1 hour"
            TWO_HOURS -> "2 hours"
            THREE_HOURS -> "3 hours"
            else -> "$minutes min"
        }
    }
    
    // 获取时间显示文本 - 使用Context获取资源字符串
    fun getDisplayText(minutes: Int, context: Context): String {
        return when (minutes) {
            THREE_MINUTES -> context.getString(R.string.minutes_3)
            FIFTEEN_MINUTES -> context.getString(R.string.minutes_15)
            THIRTY_MINUTES -> context.getString(R.string.minutes_30)
            ONE_HOUR -> context.getString(R.string.hours_1)
            TWO_HOURS -> context.getString(R.string.hours_2)
            THREE_HOURS -> context.getString(R.string.hours_3)
            else -> minutes.toString() + " " + context.getString(R.string.minutes_3).substring(1)
        }
    }
    
    // 获取时间显示文本 - Composable环境下使用
    @Composable
    fun getDisplayTextComposable(minutes: Int): String {
        return when (minutes) {
            THREE_MINUTES -> stringResource(R.string.minutes_3)
            FIFTEEN_MINUTES -> stringResource(R.string.minutes_15)
            THIRTY_MINUTES -> stringResource(R.string.minutes_30)
            ONE_HOUR -> stringResource(R.string.hours_1)
            TWO_HOURS -> stringResource(R.string.hours_2)
            THREE_HOURS -> stringResource(R.string.hours_3)
            else -> "$minutes " + stringResource(R.string.minutes_3).substring(1)
        }
    }
    
    // 获取时间框架名称 - 用于UI显示
    @Composable
    fun getTimeFrameName(timeFrame: Int): String {
        return getDisplayTextComposable(timeFrame)
    }
    
    // 确保使用分钟而不是天数来计算截止时间
    fun calculateDeadline(timeFrame: Int): Long {
        val currentTime = System.currentTimeMillis()
        // 将分钟转换为毫秒
        val durationInMillis = timeFrame * 60 * 1000L
        return currentTime + durationInMillis
    }
} 