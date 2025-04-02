package com.example.seeding.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.seeding.R
import com.example.seeding.ui.navigation.Screen
import com.example.seeding.util.LocalLanguageState

/**
 * 应用程序顶部应用栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeedingTopAppBar(
    currentRoute: String?,
    isDrawerScreen: Boolean = false,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit,
    actions: @Composable (() -> Unit)? = null // 添加右侧操作按钮
) {
    // 监听语言变化，用于强制重组
    LocalLanguageState.current.forceUpdate.value

    // 登录相关页面不显示顶部应用栏
    val isAuthScreen = currentRoute == Screen.Login.route || 
                       currentRoute == Screen.Register.route || 
                       currentRoute == Screen.ForgotPassword.route
    
    if (!isAuthScreen) {
        // 登录和启动页面不显示导航按钮
        val showNavButton = true
        
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = getScreenTitle(currentRoute),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                if (showNavButton) {
                    if (isDrawerScreen) {
                        // 抽屉菜单页面显示返回按钮
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    } else {
                        // 主页面显示菜单按钮
                        IconButton(onClick = onMenuClick) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.menu)
                            )
                        }
                    }
                }
            },
            actions = {
                // 显示右侧操作按钮（如果有）
                actions?.invoke()
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

/**
 * 创建一个操作按钮，用于顶部应用栏的右侧
 */
@Composable
fun ActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

/**
 * 根据当前路由获取屏幕标题
 */
@Composable
private fun getScreenTitle(currentRoute: String?): String {
    return when {
        currentRoute == Screen.Action.route -> stringResource(R.string.nav_action)
        currentRoute == Screen.Goal.route -> stringResource(R.string.nav_goal)
        currentRoute == Screen.Harvest.route -> stringResource(R.string.nav_harvest)
        currentRoute == Screen.Profile.route -> stringResource(R.string.nav_profile)
        currentRoute == Screen.Settings.route -> stringResource(R.string.nav_settings)
        // 设置子页面标题
        currentRoute == "${Screen.Settings.route}/language" -> "${stringResource(R.string.nav_settings)}-${stringResource(R.string.language)}"
        currentRoute == "${Screen.Settings.route}/display" -> "${stringResource(R.string.nav_settings)}-${stringResource(R.string.display_settings)}"
        currentRoute == Screen.Store.route -> stringResource(R.string.nav_store)
        currentRoute == Screen.Login.route -> stringResource(R.string.login)
        currentRoute == Screen.Register.route -> stringResource(R.string.register)
        currentRoute == Screen.ForgotPassword.route -> stringResource(R.string.forgot_password_title)
        else -> stringResource(R.string.app_name)
    }
} 