package com.example.seeding.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.example.seeding.R
import com.example.seeding.ui.components.ActionButton
import com.example.seeding.ui.components.SeedingTopAppBar
import com.example.seeding.ui.navigation.Screen
import com.example.seeding.ui.navigation.SeedingNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Grass
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Task
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import com.example.seeding.ui.theme.AppThemeManager
import com.example.seeding.ui.theme.Typography
import com.example.seeding.util.ProvideLanguageSettings
import androidx.compose.runtime.collectAsState
import com.example.seeding.util.LocalLanguageState
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeedingApp(
    navController: NavHostController = rememberNavController() // 接受参数，同时提供默认值以保持向后兼容
): Pair<androidx.compose.material3.DrawerState, @Composable () -> Unit> {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // 标记哪个底部导航栏项目被选中
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    
    // 记住上一次抽屉菜单打开时的页面，用于返回
    var lastMainScreenRoute by rememberSaveable { mutableStateOf(Screen.Action.route) }
    
    // 记住上一个路由，用于处理返回逻辑
    var previousRoute by remember { mutableStateOf<String?>(null) }
    
    // 获取主题设置
    val colorScheme = AppThemeManager.getColorScheme()
    val fontSizeScale = AppThemeManager.fontSizeScale
    
    // 返回drawerState和界面Composable函数
    return Pair(drawerState) {
        // 使用ProvideLanguageSettings包装整个应用，使语言变化能够立即生效
        ProvideLanguageSettings {
            // 应用主题设置
            MaterialTheme(
                colorScheme = colorScheme,
                // 使用字体大小缩放
                typography = Typography.copy(
                    bodyLarge = Typography.bodyLarge.copy(fontSize = Typography.bodyLarge.fontSize * fontSizeScale),
                    bodyMedium = Typography.bodyMedium.copy(fontSize = Typography.bodyMedium.fontSize * fontSizeScale),
                    bodySmall = Typography.bodySmall.copy(fontSize = Typography.bodySmall.fontSize * fontSizeScale),
                    titleLarge = Typography.titleLarge.copy(fontSize = Typography.titleLarge.fontSize * fontSizeScale),
                    titleMedium = Typography.titleMedium.copy(fontSize = Typography.titleMedium.fontSize * fontSizeScale),
                    titleSmall = Typography.titleSmall.copy(fontSize = Typography.titleSmall.fontSize * fontSizeScale),
                    labelLarge = Typography.labelLarge.copy(fontSize = Typography.labelLarge.fontSize * fontSizeScale),
                    labelMedium = Typography.labelMedium.copy(fontSize = Typography.labelMedium.fontSize * fontSizeScale),
                    labelSmall = Typography.labelSmall.copy(fontSize = Typography.labelSmall.fontSize * fontSizeScale),
                    headlineLarge = Typography.headlineLarge.copy(fontSize = Typography.headlineLarge.fontSize * fontSizeScale),
                    headlineMedium = Typography.headlineMedium.copy(fontSize = Typography.headlineMedium.fontSize * fontSizeScale),
                    headlineSmall = Typography.headlineSmall.copy(fontSize = Typography.headlineSmall.fontSize * fontSizeScale)
                )
            ) {
                // 使用当前语言状态，当语言变化时强制重组
                LocalLanguageState.current
                
                // 底部导航项目
                val bottomNavItems = listOf(
                    BottomNavItem(
                        route = Screen.Action.route,
                        selectedIcon = Icons.Filled.Spa,
                        unselectedIcon = Icons.Outlined.Spa,
                        title = stringResource(R.string.nav_action)
                    ),
                    BottomNavItem(
                        route = Screen.Goal.route,
                        selectedIcon = Icons.Filled.Task,
                        unselectedIcon = Icons.Outlined.Task,
                        title = stringResource(R.string.nav_goal)
                    ),
                    BottomNavItem(
                        route = Screen.Harvest.route,
                        selectedIcon = Icons.Filled.Grass,
                        unselectedIcon = Icons.Outlined.Grass,
                        title = stringResource(R.string.nav_harvest)
                    )
                )
                
                // 抽屉菜单项目
                val drawerItems = listOf(
                    DrawerItem(
                        route = Screen.Profile.route,
                        icon = Icons.Default.AccountCircle,
                        title = stringResource(R.string.nav_profile)
                    ),
                    DrawerItem(
                        route = Screen.Settings.route,
                        icon = Icons.Default.Settings,
                        title = stringResource(R.string.nav_settings)
                    ),
                    DrawerItem(
                        route = Screen.Store.route,
                        icon = Icons.Default.Store,
                        title = stringResource(R.string.nav_store)
                    )
                )
                
                // 当前是否在登录或启动页面
                val isAuthScreen = currentDestination?.route == Screen.Login.route || 
                                currentDestination?.route == Screen.Register.route ||
                                currentDestination?.route == Screen.ForgotPassword.route
                
                // 当前是否在抽屉菜单内的页面
                val isDrawerScreen = currentDestination?.route == Screen.Profile.route ||
                                    currentDestination?.route == Screen.Settings.route ||
                                    currentDestination?.route == Screen.SettingsLanguage.route ||
                                    currentDestination?.route == Screen.SettingsDisplay.route ||
                                    currentDestination?.route == Screen.Store.route
                
                // 仅在主导航页面启用抽屉手势
                val drawerGesturesEnabled = !isAuthScreen && !isDrawerScreen
                
                // 主页面路由列表
                val mainScreenRoutes = listOf(Screen.Action.route, Screen.Goal.route, Screen.Harvest.route)
                
                // 如果当前在主页面，更新lastMainScreenRoute
                if (currentDestination?.route in mainScreenRoutes) {
                    lastMainScreenRoute = currentDestination?.route ?: Screen.Action.route
                }
                
                // 监听导航变化，处理特殊导航逻辑
                LaunchedEffect(currentDestination?.route) {
                    val currentRoute = currentDestination?.route
                    
                    if (currentRoute != null && previousRoute != null) {
                        // 如果从抽屉菜单页面(Profile, Settings, Store)返回到主页面
                        val wasInDrawerScreen = previousRoute == Screen.Profile.route || 
                                                previousRoute == Screen.Settings.route || 
                                                previousRoute == Screen.Store.route
                                                
                        val isNowInMainScreen = currentRoute in mainScreenRoutes
                        
                        if (wasInDrawerScreen && isNowInMainScreen) {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                        
                        // 更新底部导航栏选中状态
                        when (currentRoute) {
                            Screen.Action.route -> selectedItemIndex = 0
                            Screen.Goal.route -> selectedItemIndex = 1
                            Screen.Harvest.route -> selectedItemIndex = 2
                        }
                    }
                    
                    // 更新前一个路由
                    previousRoute = currentRoute
                }
                
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = drawerGesturesEnabled,
                    scrimColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f), // 调整遮罩颜色和透明度
                    drawerContent = {
                        Box(
                            modifier = Modifier.fillMaxWidth(0.85f), // 确保宽度一致
                            contentAlignment = Alignment.CenterStart
                        ) {
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // 抽屉菜单标题
                                Text(
                                    text = "Seeding",
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // 抽屉菜单项目
                                drawerItems.forEach { item ->
                                    NavigationDrawerItem(
                                        icon = { Icon(item.icon, contentDescription = item.title) },
                                        label = { Text(text = item.title) },
                                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                // 不弹出任何页面，保留完整回栈，让系统返回键正常工作
                                                launchSingleTop = true 
                                            }
                                            // 关闭抽屉
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            SeedingTopAppBar(
                                currentRoute = currentDestination?.route,
                                isDrawerScreen = isDrawerScreen,
                                onMenuClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                },
                                onBackClick = {
                                    // 直接使用系统返回，而不是导航到特定页面
                                    navController.navigateUp()
                                },
                                actions = {
                                    // 为不同页面提供不同的操作按钮
                                    when (currentDestination?.route) {
                                        Screen.Action.route -> {
                                            // 播种页面提供搜索按钮
                                            ActionButton(
                                                icon = Icons.Default.Search,
                                                contentDescription = stringResource(R.string.search),
                                                onClick = { /* TODO: 搜索功能 */ }
                                            )
                                        }
                                        Screen.Goal.route -> {
                                            // 目标页面提供编辑按钮
                                            ActionButton(
                                                icon = Icons.Default.Edit,
                                                contentDescription = stringResource(R.string.edit_goals),
                                                onClick = {
                                                    // 使用savedStateHandle存储编辑模式状态
                                                    val currentEntry = navController.currentBackStackEntry
                                                    val savedStateHandle = currentEntry?.savedStateHandle
                                                    val currentEditMode = savedStateHandle?.get<Boolean>("isEditMode") ?: false
                                                    savedStateHandle?.set("isEditMode", !currentEditMode)
                                                }
                                            )
                                        }
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            // 只在主屏幕显示底部导航栏
                            val mainScreens = listOf(Screen.Action.route, Screen.Goal.route, Screen.Harvest.route)
                            if (currentDestination?.route in mainScreens) {
                                NavigationBar {
                                    bottomNavItems.forEachIndexed { index, item ->
                                        NavigationBarItem(
                                            icon = {
                                                Icon(
                                                    imageVector = if (selectedItemIndex == index) {
                                                        item.selectedIcon
                                                    } else {
                                                        item.unselectedIcon
                                                    },
                                                    contentDescription = item.title
                                                )
                                            },
                                            label = { Text(item.title) },
                                            selected = selectedItemIndex == index,
                                            onClick = {
                                                selectedItemIndex = index
                                                navController.navigate(item.route) {
                                                    // 修改导航逻辑，设置为顶级导航目标，而不是返回到播种页
                                                    popUpTo(navController.graph.startDestinationId) {
                                                        inclusive = true  // 包括startDestination也弹出
                                                    }
                                                    launchSingleTop = true
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            SeedingNavigation(
                                navController = navController,
                                startDestination = Screen.Login.route
                            )
                        }
                    }
                }
            }
        }
    }
}

// 底部导航项目数据类
data class BottomNavItem(
    val route: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String
)

// 抽屉菜单项目数据类
data class DrawerItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String
) 