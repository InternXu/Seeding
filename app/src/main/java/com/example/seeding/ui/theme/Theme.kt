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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 浅色主题颜色
private val LightColors = lightColorScheme(
    primary = Color(0xFF4CAF50),     // 草绿色作为主色调，象征"种子"概念
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCEDC8),
    onPrimaryContainer = Color(0xFF1B5E20),
    secondary = Color(0xFF8BC34A),   // 淡绿色作为次要色调
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCEDC8),
    onSecondaryContainer = Color(0xFF33691E),
    tertiary = Color(0xFFFFEB3B),    // 黄色代表阳光，让种子发芽
    onTertiary = Color.Black,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF121212),
    surface = Color.White,
    onSurface = Color(0xFF121212),
    error = Color(0xFFB00020),
    onError = Color.White
)

// 深色主题颜色
private val DarkColors = darkColorScheme(
    primary = Color(0xFF81C784),     // 深色主题中使用更亮的绿色
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF2E7D32),
    onPrimaryContainer = Color(0xFFC8E6C9),
    secondary = Color(0xFFAED581),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF558B2F),
    onSecondaryContainer = Color(0xFFDCEDC8),
    tertiary = Color(0xFFFFF59D),
    onTertiary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun SeedingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 动态颜色仅在Android 12及以上可用
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 