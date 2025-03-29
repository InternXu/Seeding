package com.example.seeding.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// 定义主题类型
enum class ThemeType {
    DEFAULT, // 默认主题
    NATURE,  // 自然主题
    OCEAN,   // 海洋主题
    VITALITY,  // 活力主题（原日落主题）
    FLORAL,  // 花海主题（原紫色主题）
    RETRO,    // 复古主题（原单色主题）
    HOMELAND, // 祖国主题（新增）
    MINIMAL,   // 极简主题（新增）
    SKY       // 天空主题
}

// 定义字体类型
enum class FontType {
    DEFAULT, // 默认字体
    PLAYFUL, // 活泼字体
    ELEGANT  // 优雅字体
}

// 创建主题本地存储
val LocalThemeType = staticCompositionLocalOf { ThemeType.DEFAULT }
val LocalFontType = staticCompositionLocalOf { FontType.DEFAULT }

// 默认深色主题
private val DarkColorScheme = darkColorScheme(
    primary = Green40,
    secondary = Green80,
    tertiary = Green90,
    background = DarkGreen10,
    surface = DarkGreen20,
    onPrimary = Color.White,
    onSecondary = DarkGreen40,
    onTertiary = DarkGreen40,
    onBackground = Color.White,
    onSurface = Color.White,
)

// 默认浅色主题
private val LightColorScheme = lightColorScheme(
    primary = Green40,
    secondary = Green80,
    tertiary = Green90,
    background = LightGreen10,
    surface = LightGreen20,
    onPrimary = Color.White,
    onSecondary = DarkGreen40,
    onTertiary = DarkGreen40,
    onBackground = DarkGreen40,
    onSurface = DarkGreen40,
)

// 自然深色主题
private val NatureDarkColorScheme = darkColorScheme(
    primary = Color(0xFF558B2F),
    secondary = Color(0xFF8BC34A),
    tertiary = Color(0xFFAED581),
    background = Color(0xFF1B3B00),
    surface = Color(0xFF224500),
    onPrimary = Color.White,
    onSecondary = Color(0xFF1B3B00),
    onTertiary = Color(0xFF1B3B00),
    onBackground = Color.White,
    onSurface = Color.White,
)

// 自然浅色主题
private val NatureLightColorScheme = lightColorScheme(
    primary = Color(0xFF558B2F),
    secondary = Color(0xFF8BC34A),
    tertiary = Color(0xFFAED581),
    background = Color(0xFFF1F8E9),
    surface = Color(0xFFDCEDC8),
    onPrimary = Color.White,
    onSecondary = Color(0xFF2E3B17),
    onTertiary = Color(0xFF2E3B17),
    onBackground = Color(0xFF2E3B17),
    onSurface = Color(0xFF2E3B17),
)

// 海洋深色主题
private val OceanDarkColorScheme = darkColorScheme(
    primary = Color(0xFF0288D1),
    secondary = Color(0xFF4FC3F7),
    tertiary = Color(0xFF81D4FA),
    background = Color(0xFF01579B),
    surface = Color(0xFF0277BD),
    onPrimary = Color.White,
    onSecondary = Color(0xFF01579B),
    onTertiary = Color(0xFF01579B),
    onBackground = Color.White,
    onSurface = Color.White,
)

// 海洋浅色主题
private val OceanLightColorScheme = lightColorScheme(
    primary = Color(0xFF0288D1),
    secondary = Color(0xFF4FC3F7),
    tertiary = Color(0xFF81D4FA),
    background = Color(0xFFE1F5FE),
    surface = Color(0xFFB3E5FC),
    onPrimary = Color.White,
    onSecondary = Color(0xFF01579B),
    onTertiary = Color(0xFF01579B),
    onBackground = Color(0xFF01579B),
    onSurface = Color(0xFF01579B),
)

// 默认字体样式
private val DefaultTypography = androidx.compose.material3.Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    )
)

// 活泼字体样式
private val PlayfulTypography = androidx.compose.material3.Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // 实际应用中应该使用自定义字体
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.6.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.6.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
)

// 优雅字体样式
private val ElegantTypography = androidx.compose.material3.Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Serif, // 使用衬线字体
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.4.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = -0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = -0.5.sp
    )
)

@Composable
fun SeedingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    themeType: ThemeType = ThemeType.DEFAULT,
    fontType: FontType = FontType.DEFAULT,
    content: @Composable () -> Unit
) {
    // 根据选择的主题类型和深色/浅色模式选择颜色方案
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        themeType == ThemeType.NATURE -> {
            if (darkTheme) NatureDarkColorScheme else NatureLightColorScheme
        }
        themeType == ThemeType.OCEAN -> {
            if (darkTheme) OceanDarkColorScheme else OceanLightColorScheme
        }
        else -> {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }
    }

    // 根据选择的字体类型选择排版样式
    val typography = when (fontType) {
        FontType.PLAYFUL -> PlayfulTypography
        FontType.ELEGANT -> ElegantTypography
        else -> DefaultTypography
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // 提供主题类型和字体类型的本地存储
    CompositionLocalProvider(
        LocalThemeType provides themeType,
        LocalFontType provides fontType
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}

// 获取当前主题类型
object SeedingTheme {
    val themeType: ThemeType
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeType.current

    val fontType: FontType
        @Composable
        @ReadOnlyComposable
        get() = LocalFontType.current
} 