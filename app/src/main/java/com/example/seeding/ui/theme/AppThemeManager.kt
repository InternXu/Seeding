package com.example.seeding.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

object AppThemeManager {
    // 当前主题类型
    var currentThemeType by mutableStateOf(ThemeType.DEFAULT)
        private set
    
    // 当前是否为暗色模式
    var isDarkMode by mutableStateOf(false)
        private set
    
    // 当前字体大小比例
    var fontSizeScale by mutableStateOf(1.0f)
        private set
        
    // 更新主题类型
    fun updateThemeType(themeType: ThemeType) {
        currentThemeType = themeType
    }
    
    // 更新深色模式
    fun updateDarkMode(darkMode: Boolean) {
        isDarkMode = darkMode
    }
    
    // 更新字体大小
    fun updateFontSize(fontSize: Float) {
        fontSizeScale = fontSize
    }
    
    /**
     * 保存主题设置到 SharedPreferences
     * @param context 上下文
     */
    fun saveThemeSettings(context: Context) {
        val prefs = getSharedPreferences(context)
        prefs.edit()
            .putString("theme_type", currentThemeType.name)
            .putBoolean("dark_mode", isDarkMode)
            .putFloat("font_size_scale", fontSizeScale)
            .apply()
    }
    
    /**
     * 从 SharedPreferences 加载主题设置
     * @param context 上下文
     * @return 是否成功加载设置
     */
    fun loadThemeSettings(context: Context): Boolean {
        val prefs = getSharedPreferences(context)
        
        // 加载主题类型
        val themeTypeName = prefs.getString("theme_type", null) ?: return false
        try {
            currentThemeType = ThemeType.valueOf(themeTypeName)
        } catch (e: IllegalArgumentException) {
            // 如果保存的主题类型无效，使用默认主题
            currentThemeType = ThemeType.DEFAULT
        }
        
        // 加载深色模式设置
        isDarkMode = prefs.getBoolean("dark_mode", false)
        
        // 加载字体大小设置
        fontSizeScale = prefs.getFloat("font_size_scale", 1.0f)
        
        return true
    }
    
    /**
     * 获取 SharedPreferences
     */
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    }
    
    // 获取当前颜色方案
    fun getColorScheme(): ColorScheme {
        return when (currentThemeType) {
            ThemeType.DEFAULT -> {
                if (isDarkMode) {
                    darkColorScheme(
                        primary = Color(0xFFD0BCFF),
                        onPrimary = Color(0xFF381E72),
                        primaryContainer = Color(0xFF4F378B),
                        onPrimaryContainer = Color(0xFFEADDFF),
                        secondary = Color(0xFFCCC2DC),
                        onSecondary = Color(0xFF332D41),
                        secondaryContainer = Color(0xFF4A4458),
                        onSecondaryContainer = Color(0xFFE8DEF8),
                        tertiary = Color(0xFFEFB8C8),
                        onTertiary = Color(0xFF492532),
                        tertiaryContainer = Color(0xFF633B48),
                        onTertiaryContainer = Color(0xFFFFD8E4)
                    )
                } else {
                    lightColorScheme(
                        primary = Color(0xFF6750A4),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFEADDFF),
                        onPrimaryContainer = Color(0xFF21005E),
                        secondary = Color(0xFF625B71),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFE8DEF8),
                        onSecondaryContainer = Color(0xFF1E192B),
                        tertiary = Color(0xFF7D5260),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFFFD8E4),
                        onTertiaryContainer = Color(0xFF370B1E)
                    )
                }
            }
            ThemeType.NATURE -> {
                if (isDarkMode) {
                    darkColorScheme(
                        primary = Color(0xFF9CCC65),
                        onPrimary = Color(0xFF1B3B00),
                        primaryContainer = Color(0xFF2C5600),
                        onPrimaryContainer = Color(0xFFBBF780),
                        secondary = Color(0xFF8BC34A),
                        onSecondary = Color(0xFF0A2E00),
                        secondaryContainer = Color(0xFF1F4700),
                        onSecondaryContainer = Color(0xFFA0E559),
                        tertiary = Color(0xFFAED581),
                        onTertiary = Color(0xFF133800),
                        tertiaryContainer = Color(0xFF1F5200),
                        onTertiaryContainer = Color(0xFFC8F19D),
                        background = Color(0xFF1A2E00),  // 深绿黑背景
                        onBackground = Color(0xFFD7FFB9),
                        surface = Color(0xFF1A2E00),
                        onSurface = Color(0xFFD7FFB9)
                    )
                } else {
                    lightColorScheme(
                        primary = Color(0xFF558B2F),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFCBE6A8),
                        onPrimaryContainer = Color(0xFF0C2000),
                        secondary = Color(0xFF33691E),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFA0D568),
                        onSecondaryContainer = Color(0xFF0A2000),
                        tertiary = Color(0xFF689F38),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFCBE6A8),
                        onTertiaryContainer = Color(0xFF142800),
                        background = Color(0xFFF1F8E9),  // 淡绿背景
                        onBackground = Color(0xFF1B3B00),
                        surface = Color(0xFFF1F8E9),
                        onSurface = Color(0xFF1B3B00)
                    )
                }
            }
            ThemeType.OCEAN -> {
                if (isDarkMode) {
                    darkColorScheme(
                        primary = Color(0xFF0277BD),  // 更深的蓝色
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFF01579B),  // 深蓝容器
                        onPrimaryContainer = Color(0xFFB8EAFF),
                        secondary = Color(0xFF0D47A1),  // 更深的次级蓝
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFF002171),
                        onSecondaryContainer = Color(0xFF97F0FF),
                        tertiary = Color(0xFF0288D1),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFF01579B),
                        onTertiaryContainer = Color(0xFFBBE9FF),
                        background = Color(0xFF001E33),  // 深蓝黑背景
                        onBackground = Color(0xFFFFFFFF),
                        surface = Color(0xFF001E33),
                        onSurface = Color(0xFFFFFFFF)
                    )
                } else {
                    lightColorScheme(
                        primary = Color(0xFF01579B),  // 更深的蓝色
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFCFE4FF),
                        onPrimaryContainer = Color(0xFF001D36),
                        secondary = Color(0xFF0D47A1),  // 更深的次级蓝
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFD0EFFF),
                        onSecondaryContainer = Color(0xFF001D36),
                        tertiary = Color(0xFF0277BD),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFCFE4FF),
                        onTertiaryContainer = Color(0xFF001D36),
                        background = Color(0xFFEDF7FF),  // 浅蓝背景
                        onBackground = Color(0xFF001833),
                        surface = Color(0xFFEDF7FF),
                        onSurface = Color(0xFF001833)
                    )
                }
            }
            ThemeType.VITALITY -> {
                if (isDarkMode) {
                    darkColorScheme(
                        primary = Color(0xFFFF7043),
                        onPrimary = Color(0xFF3E1400),
                        primaryContainer = Color(0xFF5C2200),
                        onPrimaryContainer = Color(0xFFFFDBCF),
                        secondary = Color(0xFFFFAB91),
                        onSecondary = Color(0xFF481A09),
                        secondaryContainer = Color(0xFF662B11),
                        onSecondaryContainer = Color(0xFFFFDBD0),
                        tertiary = Color(0xFFFFD54F),
                        onTertiary = Color(0xFF433200),
                        tertiaryContainer = Color(0xFF604600),
                        onTertiaryContainer = Color(0xFFFFECB5),
                        background = Color(0xFF2C1500),  // 深棕红色背景
                        onBackground = Color(0xFFFFDBCF),
                        surface = Color(0xFF2C1500),
                        onSurface = Color(0xFFFFDBCF)
                    )
                } else {
                    lightColorScheme(
                        primary = Color(0xFFE64A19),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFFFDBD0),
                        onPrimaryContainer = Color(0xFF3E1100),
                        secondary = Color(0xFFFF5722),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFFFDBD0),
                        onSecondaryContainer = Color(0xFF3E1100),
                        tertiary = Color(0xFFFFC107),
                        onTertiary = Color(0xFF000000),
                        tertiaryContainer = Color(0xFFFFF8E1),
                        onTertiaryContainer = Color(0xFF332800),
                        background = Color(0xFFFBE9E7),  // 淡橙色背景
                        onBackground = Color(0xFF3E1100),
                        surface = Color(0xFFFBE9E7),
                        onSurface = Color(0xFF3E1100)
                    )
                }
            }
            ThemeType.FLORAL -> {
                if (isDarkMode) {
                    darkColorScheme(
                        primary = Color(0xFFBB86FC),
                        onPrimary = Color(0xFF380C7A),
                        primaryContainer = Color(0xFF4F1D95),
                        onPrimaryContainer = Color(0xFFEEDCFF),
                        secondary = Color(0xFF7C4DFF),
                        onSecondary = Color(0xFF2A0099),
                        secondaryContainer = Color(0xFF3A00CC),
                        onSecondaryContainer = Color(0xFFE5DEFF),
                        tertiary = Color(0xFF9C27B0),
                        onTertiary = Color(0xFF650073),
                        tertiaryContainer = Color(0xFF8000A1),
                        onTertiaryContainer = Color(0xFFFFD8FF),
                        background = Color(0xFF2C0050),  // 深紫色背景
                        onBackground = Color(0xFFEEDCFF),
                        surface = Color(0xFF2C0050),
                        onSurface = Color(0xFFEEDCFF)
                    )
                } else {
                    lightColorScheme(
                        primary = Color(0xFF8E24AA),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFEADCFF),
                        onPrimaryContainer = Color(0xFF380C7A),
                        secondary = Color(0xFF6200EE),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFE4DDFF),
                        onSecondaryContainer = Color(0xFF2A0099),
                        tertiary = Color(0xFF9C27B0),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFFFD8FF),
                        onTertiaryContainer = Color(0xFF530073),
                        background = Color(0xFFF3E5F5),  // 淡紫色背景
                        onBackground = Color(0xFF380C7A),
                        surface = Color(0xFFF3E5F5),
                        onSurface = Color(0xFF380C7A)
                    )
                }
            }
            ThemeType.RETRO -> {
                if (isDarkMode) {
                    darkColorScheme(
                        primary = Color(0xFFDDDDDD),
                        onPrimary = Color(0xFF202020),
                        primaryContainer = Color(0xFF444444),
                        onPrimaryContainer = Color(0xFFEEEEEE),
                        secondary = Color(0xFFBBBBBB),
                        onSecondary = Color(0xFF101010),
                        secondaryContainer = Color(0xFF333333),
                        onSecondaryContainer = Color(0xFFCCCCCC),
                        tertiary = Color(0xFF999999),
                        onTertiary = Color(0xFF101010),
                        tertiaryContainer = Color(0xFF222222),
                        onTertiaryContainer = Color(0xFFAAAAAA),
                        background = Color(0xFF121212),  // 暗灰色背景
                        onBackground = Color(0xFFEEEEEE),
                        surface = Color(0xFF121212),
                        onSurface = Color(0xFFEEEEEE)
                    )
                } else {
                    lightColorScheme(
                        primary = Color(0xFF333333),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFDDDDDD),
                        onPrimaryContainer = Color(0xFF202020),
                        secondary = Color(0xFF555555),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFEEEEEE),
                        onSecondaryContainer = Color(0xFF202020),
                        tertiary = Color(0xFF777777),
                        onTertiary = Color(0xFFFFFFFF),
                        tertiaryContainer = Color(0xFFF5F5F5),
                        onTertiaryContainer = Color(0xFF101010),
                        background = Color(0xFFF5F5F5),  // 浅灰色背景
                        onBackground = Color(0xFF202020),
                        surface = Color(0xFFF5F5F5),
                        onSurface = Color(0xFF202020)
                    )
                }
            }
            ThemeType.HOMELAND -> {
                if (isDarkMode) {
                    darkColorScheme(
                        primary = Color(0xFFE53935),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFF7F0000),
                        onPrimaryContainer = Color(0xFFFFCDD2),
                        secondary = Color(0xFFB71C1C),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFF5F0000),
                        onSecondaryContainer = Color(0xFFFFCCBC),
                        tertiary = Color(0xFFFFC107),
                        onTertiary = Color(0xFF000000),
                        tertiaryContainer = Color(0xFF805600),
                        onTertiaryContainer = Color(0xFFFFE082),
                        background = Color(0xFF5F0000),  // 深红色背景
                        onBackground = Color(0xFFFFCDD2),
                        surface = Color(0xFF5F0000),
                        onSurface = Color(0xFFFFCDD2)
                    )
                } else {
                    lightColorScheme(
                        primary = Color(0xFFE53935),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFFFCDD2),
                        onPrimaryContainer = Color(0xFF7F0000),
                        secondary = Color(0xFFB71C1C),
                        onSecondary = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFFFFCCBC),
                        onSecondaryContainer = Color(0xFF5F0000),
                        tertiary = Color(0xFFFFC107),
                        onTertiary = Color(0xFF000000),
                        tertiaryContainer = Color(0xFFFFECB3),
                        onTertiaryContainer = Color(0xFF805600),
                        background = Color(0xFFFEF2F2),  // 淡红色背景
                        onBackground = Color(0xFF7F0000),
                        surface = Color(0xFFFEF2F2),
                        onSurface = Color(0xFF7F0000)
                    )
                }
            }
            ThemeType.MINIMAL -> {
                if (isDarkMode) {
                    darkColorScheme(
                        // 标题栏色
                        primary = Color(0xFF000000),  // 纯黑，与背景一致
                        onPrimary = Color(0xFFFFFFFF), // 白色文字
                        
                        // 按钮和控件颜色 - 使用其他颜色属性保持可见性
                        secondary = Color(0xFF555555),  // 中灰色按钮
                        onSecondary = Color(0xFFFFFFFF), 
                        tertiary = Color(0xFF777777),   // 浅灰色其他控件
                        onTertiary = Color(0xFFFFFFFF),
                        
                        // 背景和表面
                        background = Color(0xFF000000),
                        onBackground = Color(0xFFFFFFFF),
                        surface = Color(0xFF000000),
                        onSurface = Color(0xFFFFFFFF),
                        
                        // 容器
                        primaryContainer = Color(0xFF333333), // 用于浮动按钮(+号按钮)
                        onPrimaryContainer = Color(0xFFFFFFFF),
                        secondaryContainer = Color(0xFF222222),
                        onSecondaryContainer = Color(0xFFDDDDDD),
                        tertiaryContainer = Color(0xFF252525),
                        onTertiaryContainer = Color(0xFFDDDDDD),
                        errorContainer = Color(0xFF331C1E),
                        onErrorContainer = Color(0xFFCF6679),
                        
                        // 其他表面颜色
                        surfaceVariant = Color(0xFF121212),
                        onSurfaceVariant = Color(0xFFDDDDDD),
                        inverseSurface = Color(0xFFDDDDDD),
                        inverseOnSurface = Color(0xFF121212),
                        surfaceTint = Color.Transparent,
                        
                        // 边框和错误状态
                        outline = Color(0xFF757575),
                        error = Color(0xFFCF6679),
                        onError = Color(0xFF000000),
                        outlineVariant = Color(0xFF444444),
                        scrim = Color(0x80000000)
                    )
                } else {
                    lightColorScheme(
                        primary = Color(0xFF757575),
                        onPrimary = Color(0xFFFFFFFF),
                        primaryContainer = Color(0xFFE0E0E0),
                        onPrimaryContainer = Color(0xFF424242),
                        secondary = Color(0xFF9E9E9E),
                        onSecondary = Color(0xFF000000),
                        secondaryContainer = Color(0xFFF5F5F5),
                        onSecondaryContainer = Color(0xFF424242),
                        tertiary = Color(0xFFBDBDBD),
                        onTertiary = Color(0xFF000000),
                        tertiaryContainer = Color(0xFFFAFAFA),
                        onTertiaryContainer = Color(0xFF424242),
                        background = Color(0xFFFFFFFF),
                        onBackground = Color(0xFF212121),
                        surface = Color(0xFFFFFFFF),
                        onSurface = Color(0xFF212121)
                    )
                }
            }
            ThemeType.SKY -> {
                if (isDarkMode) {
                    darkColorScheme(
                        primary = Color(0xFF03A9F4),  // 浅蓝色
                        onPrimary = Color(0xFF000000),
                        primaryContainer = Color(0xFF0288D1),
                        onPrimaryContainer = Color(0xFFE1F5FE),
                        secondary = Color(0xFF4FC3F7),
                        onSecondary = Color(0xFF000000),
                        secondaryContainer = Color(0xFF039BE5),
                        onSecondaryContainer = Color(0xFFE1F5FE),
                        tertiary = Color(0xFF81D4FA),
                        onTertiary = Color(0xFF000000),
                        tertiaryContainer = Color(0xFF0288D1),
                        onTertiaryContainer = Color(0xFFE1F5FE),
                        background = Color(0xFF01579B),
                        onBackground = Color(0xFFE1F5FE),
                        surface = Color(0xFF01579B),
                        onSurface = Color(0xFFE1F5FE)
                    )
                } else {
                    lightColorScheme(
                        primary = Color(0xFF03A9F4),  // 浅蓝色
                        onPrimary = Color(0xFF000000),
                        primaryContainer = Color(0xFFE1F5FE),
                        onPrimaryContainer = Color(0xFF01579B),
                        secondary = Color(0xFF29B6F6),
                        onSecondary = Color(0xFF000000),
                        secondaryContainer = Color(0xFFE1F5FE),
                        onSecondaryContainer = Color(0xFF01579B),
                        tertiary = Color(0xFF4FC3F7),
                        onTertiary = Color(0xFF000000),
                        tertiaryContainer = Color(0xFFE1F5FE),
                        onTertiaryContainer = Color(0xFF01579B),
                        background = Color(0xFFE1F5FE),
                        onBackground = Color(0xFF01579B),
                        surface = Color(0xFFE1F5FE),
                        onSurface = Color(0xFF01579B)
                    )
                }
            }
        }
    }
} 