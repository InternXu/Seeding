package com.example.seeding.util

import androidx.compose.ui.graphics.Color
import com.example.seeding.R
import com.example.seeding.data.model.Seed
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

/**
 * 种子工具类
 */
object SeedUtils {
    // 种子ID常量
    const val SEED_LIFE = 1        // 生命
    const val SEED_PROPERTY = 2    // 财物
    const val SEED_RELATIONSHIP = 3 // 伴侣
    const val SEED_HONESTY = 4     // 诚实
    const val SEED_HARMONY = 5     // 和谐
    const val SEED_GENTLENESS = 6  // 柔和
    const val SEED_MEANING = 7     // 含义
    const val SEED_REJOICE = 8     // 随喜
    const val SEED_COMPASSION = 9  // 同情
    const val SEED_MINDFULNESS = 10 // 正念
    const val SEED_GRATITUDE = 11  // 感恩
    const val SEED_SURPRISE = 12   // 提前

    // 获取所有种子ID列表
    fun getAllSeedIds(): List<Int> = listOf(
        SEED_LIFE, SEED_PROPERTY, SEED_RELATIONSHIP, SEED_HONESTY,
        SEED_HARMONY, SEED_GENTLENESS, SEED_MEANING, SEED_REJOICE,
        SEED_COMPASSION, SEED_MINDFULNESS, SEED_GRATITUDE, SEED_SURPRISE
    )

    // 根据种子ID获取字符串资源ID
    fun getSeedNameResId(seedId: Int): Int {
        return when (seedId) {
            SEED_LIFE -> R.string.seed_life
            SEED_PROPERTY -> R.string.seed_property
            SEED_RELATIONSHIP -> R.string.seed_relationship
            SEED_HONESTY -> R.string.seed_honesty
            SEED_HARMONY -> R.string.seed_harmony
            SEED_GENTLENESS -> R.string.seed_gentleness
            SEED_MEANING -> R.string.seed_meaning
            SEED_REJOICE -> R.string.seed_rejoice
            SEED_COMPASSION -> R.string.seed_compassion
            SEED_MINDFULNESS -> R.string.seed_mindfulness
            SEED_GRATITUDE -> R.string.seed_gratitude
            SEED_SURPRISE -> R.string.seed_surprise
            else -> R.string.seed_life
        }
    }
    
    // 根据种子ID获取种子全名的字符串资源ID
    fun getSeedFullNameResId(seedId: Int): Int {
        return when (seedId) {
            SEED_LIFE -> R.string.seed_life_full
            SEED_PROPERTY -> R.string.seed_property_full
            SEED_RELATIONSHIP -> R.string.seed_relationship_full
            SEED_HONESTY -> R.string.seed_honesty_full
            SEED_HARMONY -> R.string.seed_harmony_full
            SEED_GENTLENESS -> R.string.seed_gentleness_full
            SEED_MEANING -> R.string.seed_meaning_full
            SEED_REJOICE -> R.string.seed_rejoice_full
            SEED_COMPASSION -> R.string.seed_compassion_full
            SEED_MINDFULNESS -> R.string.seed_mindfulness_full
            SEED_GRATITUDE -> R.string.seed_gratitude_full
            SEED_SURPRISE -> R.string.seed_surprise_full
            else -> R.string.seed_life_full
        }
    }
    
    // 获取种子简称
    fun getSeedAbbreviation(seedId: Int): String {
        return when (seedId) {
            SEED_LIFE -> "生命"
            SEED_PROPERTY -> "财物"
            SEED_RELATIONSHIP -> "伴侣"
            SEED_HONESTY -> "诚实"
            SEED_HARMONY -> "和谐"
            SEED_GENTLENESS -> "柔和"
            SEED_MEANING -> "含义"
            SEED_REJOICE -> "随喜"
            SEED_COMPASSION -> "同情"
            SEED_MINDFULNESS -> "正念"
            SEED_GRATITUDE -> "感恩"
            SEED_SURPRISE -> "提前"
            else -> ""
        }
    }
    
    // 获取种子全称 - 非Composable环境使用
    fun getSeedFullName(seedId: Int, context: Context): String {
        val resId = getSeedFullNameResId(seedId)
        return context.getString(resId)
    }
    
    // 获取种子全称 - 兼容旧代码，但不推荐使用
    fun getSeedFullName(seedId: Int): String {
        return when (seedId) {
            SEED_LIFE -> "保护生命"
            SEED_PROPERTY -> "尊重他人财物"
            SEED_RELATIONSHIP -> "尊重他人的伴侣关系"
            SEED_HONESTY -> "诚实言语"
            SEED_HARMONY -> "和谐言语"
            SEED_GENTLENESS -> "柔和言语"
            SEED_MEANING -> "有意义语言"
            SEED_REJOICE -> "随喜他人的成功"
            SEED_COMPASSION -> "同情他人的不幸"
            SEED_MINDFULNESS -> "维持正确的世界观"
            SEED_GRATITUDE -> "感恩"
            SEED_SURPRISE -> "提前和惊喜"
            else -> ""
        }
    }
    
    // 获取种子全称 - Composable环境使用
    @Composable
    fun getSeedFullNameComposable(seedId: Int): String {
        return stringResource(getSeedFullNameResId(seedId))
    }

    // 获取种子颜色
    fun getSeedColor(seedId: Int): Color {
        return when (seedId) {
            SEED_LIFE -> Color(0xFFF44336)         // 红色
            SEED_PROPERTY -> Color(0xFFFF9800)     // 橙色
            SEED_RELATIONSHIP -> Color(0xFFE91E63) // 粉色
            SEED_HONESTY -> Color(0xFF2196F3)      // 蓝色
            SEED_HARMONY -> Color(0xFF4CAF50)      // 绿色
            SEED_GENTLENESS -> Color(0xFF9C27B0)   // 紫色
            SEED_MEANING -> Color(0xFF607D8B)      // 蓝灰色
            SEED_REJOICE -> Color(0xFFFFEB3B)      // 黄色
            SEED_COMPASSION -> Color(0xFF795548)   // 棕色
            SEED_MINDFULNESS -> Color(0xFF3F51B5)  // 靛蓝色
            SEED_GRATITUDE -> Color(0xFF009688)    // 青色
            SEED_SURPRISE -> Color(0xFFFF5722)     // 深橙色
            else -> Color.Gray
        }
    }
    
    // 获取所有种子的模型列表，用于初始化数据库
    fun getAllSeedModels(): List<Seed> {
        return getAllSeedIds().map { seedId ->
            Seed(
                id = seedId,
                name = getSeedAbbreviation(seedId),
                fullName = getSeedFullName(seedId)
            )
        }
    }
} 