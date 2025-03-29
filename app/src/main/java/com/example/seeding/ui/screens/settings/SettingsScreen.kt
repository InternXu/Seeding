package com.example.seeding.ui.screens.settings

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.seeding.R
import com.example.seeding.ui.theme.ThemeType
import com.example.seeding.ui.theme.FontType
import com.example.seeding.ui.theme.AppThemeManager
import kotlin.math.roundToInt
import com.example.seeding.ui.navigation.Screen
import androidx.compose.ui.platform.LocalContext
import com.example.seeding.util.LanguageManager
import com.example.seeding.util.LocalLanguageState

// 设置页面类型
enum class SettingsPage {
    MAIN,       // 主设置页
    LANGUAGE,   // 语言设置页
    DISPLAY     // 显示设置页
}

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentPage by remember { mutableStateOf(SettingsPage.MAIN) }
    val scrollState = rememberScrollState()
    
    // 监听语言变化，用于强制界面刷新
    val currentLanguageState = LocalLanguageState.current
    val forceUpdate = currentLanguageState.forceUpdate.value
    
    // 初始化ViewModel状态与AppThemeManager同步
    LaunchedEffect(Unit) {
        // 从全局状态更新ViewModel状态
        viewModel.updateThemeType(AppThemeManager.currentThemeType)
        viewModel.updateDarkMode(AppThemeManager.isDarkMode)
        viewModel.updateFontSize(AppThemeManager.fontSizeScale)
    }
    
    // 当语言变化时，更新ViewModel中的语言状态
    LaunchedEffect(forceUpdate) {
        val currentLanguageName = LanguageManager.getCurrentLanguageName()
        if (uiState.language != currentLanguageName) {
            viewModel.updateLanguageNoSwitch(currentLanguageName)
        }
    }
    
    // 获取当前实际路由
    val currentRoute = navController.currentBackStackEntry?.destination?.route ?: Screen.Settings.route
    
    // 根据路由初始化页面状态
    LaunchedEffect(currentRoute) {
        currentPage = when(currentRoute) {
            Screen.SettingsLanguage.route -> SettingsPage.LANGUAGE
            Screen.SettingsDisplay.route -> SettingsPage.DISPLAY
            else -> SettingsPage.MAIN
        }
    }
    
    // 监听返回事件，处理子页面返回主设置页面
    BackHandler(enabled = currentPage != SettingsPage.MAIN) {
        // 当不在主页面时，返回到主设置页面而不是退出设置
        currentPage = SettingsPage.MAIN
        // 更新导航状态以匹配当前页面
        navController.popBackStack(Screen.Settings.route, false)
    }
    
    // 处理页面切换和导航同步
    LaunchedEffect(currentPage) {
        val targetRoute = when(currentPage) {
            SettingsPage.MAIN -> Screen.Settings.route
            SettingsPage.LANGUAGE -> Screen.SettingsLanguage.route
            SettingsPage.DISPLAY -> Screen.SettingsDisplay.route
        }
        
        // 如果路由不匹配当前页面，更新路由
        if (currentRoute != targetRoute) {
            if (currentPage == SettingsPage.MAIN) {
                navController.popBackStack(Screen.Settings.route, false)
            } else {
                navController.navigate(targetRoute) {
                    // 子页面作为当前页面的分支，避免建立新的页面栈
                    popUpTo(Screen.Settings.route) { inclusive = false }
                }
            }
        }
    }

    // 主内容
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        when (currentPage) {
            SettingsPage.MAIN -> {
                // 主设置页 - 仅显示选项列表
                SettingsItem(
                    icon = Icons.Outlined.Language,
                    title = stringResource(R.string.language),
                    subtitle = LanguageManager.getCurrentLanguageName(), // 直接获取当前语言
                    onClick = { currentPage = SettingsPage.LANGUAGE }
                )
                
                SettingsItem(
                    icon = Icons.Outlined.Palette,
                    title = stringResource(R.string.display_settings),
                    subtitle = getThemeDisplayName(uiState.themeType),
                    onClick = { currentPage = SettingsPage.DISPLAY }
                )
            }
            
            SettingsPage.LANGUAGE -> {
                // 语言设置页
                val languages = listOf(
                    stringResource(R.string.language_zh_cn),
                    stringResource(R.string.language_zh_tw),
                    stringResource(R.string.language_en),
                    stringResource(R.string.language_ja)
                )
                
                languages.forEach { language ->
                    SettingsRadioItem(
                        title = language,
                        isSelected = language == uiState.language,
                        onClick = { 
                            // 更新语言，传递Context确保立即生效
                            viewModel.updateLanguage(language)
                        }
                    )
                }
            }
            
            SettingsPage.DISPLAY -> {
                // 显示设置页
                
                // 主题颜色选择
                SectionTitle(title = stringResource(R.string.theme_color))
                
                // 第一行主题
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ThemeColorItem(
                        name = stringResource(R.string.theme_default),
                        color = Color(0xFF6200EE),
                        isSelected = uiState.themeType == ThemeType.DEFAULT,
                        onClick = { 
                            viewModel.updateThemeType(ThemeType.DEFAULT)
                            AppThemeManager.updateThemeType(ThemeType.DEFAULT)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    
                    ThemeColorItem(
                        name = stringResource(R.string.theme_nature),
                        color = Color(0xFF33691E),
                        isSelected = uiState.themeType == ThemeType.NATURE,
                        onClick = { 
                            viewModel.updateThemeType(ThemeType.NATURE)
                            AppThemeManager.updateThemeType(ThemeType.NATURE)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    
                    ThemeColorItem(
                        name = stringResource(R.string.theme_ocean),
                        color = Color(0xFF01579B),
                        isSelected = uiState.themeType == ThemeType.OCEAN,
                        onClick = { 
                            viewModel.updateThemeType(ThemeType.OCEAN)
                            AppThemeManager.updateThemeType(ThemeType.OCEAN)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // 第二行主题
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ThemeColorItem(
                        name = stringResource(R.string.theme_vitality),
                        color = Color(0xFFE64A19),
                        isSelected = uiState.themeType == ThemeType.VITALITY,
                        onClick = { 
                            viewModel.updateThemeType(ThemeType.VITALITY)
                            AppThemeManager.updateThemeType(ThemeType.VITALITY)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    
                    ThemeColorItem(
                        name = stringResource(R.string.theme_floral),
                        color = Color(0xFF8E24AA),
                        isSelected = uiState.themeType == ThemeType.FLORAL,
                        onClick = { 
                            viewModel.updateThemeType(ThemeType.FLORAL)
                            AppThemeManager.updateThemeType(ThemeType.FLORAL)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    
                    ThemeColorItem(
                        name = stringResource(R.string.theme_retro),
                        color = Color(0xFF333333),
                        isSelected = uiState.themeType == ThemeType.RETRO,
                        onClick = { 
                            viewModel.updateThemeType(ThemeType.RETRO) 
                            AppThemeManager.updateThemeType(ThemeType.RETRO)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // 第三行主题
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ThemeColorItem(
                        name = stringResource(R.string.theme_homeland),
                        color = Color(0xFFE53935),
                        isSelected = uiState.themeType == ThemeType.HOMELAND,
                        onClick = { 
                            viewModel.updateThemeType(ThemeType.HOMELAND)
                            AppThemeManager.updateThemeType(ThemeType.HOMELAND)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    
                    ThemeColorItem(
                        name = stringResource(R.string.theme_minimal),
                        color = Color(0xFF757575),
                        isSelected = uiState.themeType == ThemeType.MINIMAL,
                        onClick = { 
                            viewModel.updateThemeType(ThemeType.MINIMAL)
                            AppThemeManager.updateThemeType(ThemeType.MINIMAL)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    
                    ThemeColorItem(
                        name = stringResource(R.string.theme_sky),
                        color = Color(0xFF03A9F4),
                        isSelected = uiState.themeType == ThemeType.SKY,
                        onClick = { 
                            viewModel.updateThemeType(ThemeType.SKY)
                            AppThemeManager.updateThemeType(ThemeType.SKY)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                // 深色模式设置
                SettingsSwitchItem(
                    title = stringResource(R.string.dark_mode),
                    isChecked = uiState.isDarkMode,
                    onCheckedChange = { 
                        viewModel.updateDarkMode(it)
                        // 立即应用深色模式
                        AppThemeManager.updateDarkMode(it)
                    }
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                // 字体大小调整
                SectionTitle(title = stringResource(R.string.font_size))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("A", style = MaterialTheme.typography.bodySmall)
                    
                    Slider(
                        value = uiState.fontSize,
                        onValueChange = { 
                            viewModel.updateFontSize(it)
                            // 立即应用字体大小
                            AppThemeManager.updateFontSize(it)
                        },
                        valueRange = 0.8f..1.2f,
                        modifier = Modifier.weight(1f),
                        colors = if (AppThemeManager.currentThemeType == ThemeType.MINIMAL && AppThemeManager.isDarkMode) {
                            // 极简主题深色模式特殊处理
                            SliderDefaults.colors(
                                thumbColor = Color(0xFFAAAAAA),
                                activeTrackColor = Color(0xFF777777),
                                inactiveTrackColor = Color(0xFF444444)
                            )
                        } else {
                            SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary,
                                inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        }
                    )
                    
                    Text("A", style = MaterialTheme.typography.headlineSmall)
                }
                
                Text(
                    text = stringResource(R.string.font_size_percentage, (uiState.fontSize * 100).roundToInt()),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
    )
}

/**
 * 设置项组件 - 用于主设置页面的入口项
 */
@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (AppThemeManager.currentThemeType == ThemeType.MINIMAL && AppThemeManager.isDarkMode) {
                Color(0xFFAAAAAA) // 浅灰色，确保在黑色背景上可见
            } else if (AppThemeManager.currentThemeType == ThemeType.MINIMAL && !AppThemeManager.isDarkMode) {
                Color(0xFF555555) // 中灰色，适合浅色背景
            } else {
                MaterialTheme.colorScheme.primary
            },
            modifier = Modifier.size(24.dp)
        )
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Text(
            text = ">",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    
    Divider()
}

/**
 * 开关选项组件
 */
@Composable
fun SettingsSwitchItem(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                // 选中状态颜色
                checkedThumbColor = when (AppThemeManager.currentThemeType) {
                    ThemeType.MINIMAL -> if (AppThemeManager.isDarkMode) Color(0xFFAAAAAA) else Color(0xFF333333)
                    else -> MaterialTheme.colorScheme.primary // 其他主题使用各自的主题色
                },
                checkedTrackColor = when (AppThemeManager.currentThemeType) {
                    ThemeType.MINIMAL -> if (AppThemeManager.isDarkMode) Color(0xFF555555) else Color(0xFFAAAAAA)
                    else -> MaterialTheme.colorScheme.primaryContainer
                },
                // 未选中状态颜色 - 为所有主题提供可见的颜色
                uncheckedThumbColor = when (AppThemeManager.currentThemeType) {
                    ThemeType.MINIMAL -> if (AppThemeManager.isDarkMode) Color(0xFF666666) else Color(0xFF757575)
                    else -> Color(0xFF8E8E8E) // 通用灰色，确保在所有主题下可见
                },
                uncheckedTrackColor = when (AppThemeManager.currentThemeType) {
                    ThemeType.MINIMAL -> if (AppThemeManager.isDarkMode) Color(0xFF333333) else Color(0xFFDDDDDD)
                    else -> Color(0xFFDDDDDD) // 浅灰色轨道
                },
                uncheckedBorderColor = when (AppThemeManager.currentThemeType) {
                    ThemeType.MINIMAL -> if (AppThemeManager.isDarkMode) Color(0xFF666666) else Color(0xFF555555)
                    else -> Color(0xFFAAAAAA) // 中灰色边框
                }
            )
        )
    }
}

/**
 * 单选按钮选项组件
 */
@Composable
fun SettingsRadioItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
    }
    
    Divider()
}

/**
 * 主题颜色选择项
 */
@Composable
fun ThemeColorItem(
    name: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Text(
                    text = "✓",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )
    }
}

// 获取主题显示名称
@Composable
fun getThemeDisplayName(themeType: ThemeType): String {
    return when (themeType) {
        ThemeType.DEFAULT -> stringResource(R.string.theme_default)
        ThemeType.NATURE -> stringResource(R.string.theme_nature)
        ThemeType.OCEAN -> stringResource(R.string.theme_ocean)
        ThemeType.VITALITY -> stringResource(R.string.theme_vitality)
        ThemeType.FLORAL -> stringResource(R.string.theme_floral)
        ThemeType.RETRO -> stringResource(R.string.theme_retro)
        ThemeType.HOMELAND -> stringResource(R.string.theme_homeland)
        ThemeType.MINIMAL -> stringResource(R.string.theme_minimal)
        ThemeType.SKY -> stringResource(R.string.theme_sky)
    }
} 