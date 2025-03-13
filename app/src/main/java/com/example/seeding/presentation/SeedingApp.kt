package com.example.seeding.presentation

// 需要在app/build.gradle.kts中添加:
// implementation("androidx.compose.material:material-icons-extended:1.6.0")

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
// 导入扩展图标库中的图标
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.outlined.Grass
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * 应用程序的主Compose组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeedingApp() {
    // 当前选中的底部导航项
    var selectedItem by remember { mutableStateOf(0) }
    
    // 当前选中的抽屉菜单项
    var selectedDrawerItem by remember { mutableStateOf(0) }
    
    // 底部导航项
    val navigationItems = listOf(
        NavigationItem(
            title = "播种",
            selectedIcon = Icons.Filled.Eco,
            unselectedIcon = Icons.Outlined.Eco
        ),
        NavigationItem(
            title = "目标",
            selectedIcon = Icons.Filled.EmojiEvents,
            unselectedIcon = Icons.Outlined.EmojiEvents
        ),
        NavigationItem(
            title = "收获",
            selectedIcon = Icons.Filled.Grass,
            unselectedIcon = Icons.Outlined.Grass
        )
    )
    
    // 抽屉菜单项
    val drawerItems = listOf(
        DrawerItem(
            title = "账户",
            icon = Icons.Filled.Person,
            screen = { AccountScreen() }
        ),
        DrawerItem(
            title = "插件商店",
            icon = Icons.Filled.Info,
            screen = { PluginStoreScreen() }
        ),
        DrawerItem(
            title = "设置",
            icon = Icons.Filled.Settings,
            screen = { SettingsScreen() }
        )
    )
    
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // 用户信息和种子币余额
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 用户头像
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "用户头像",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // 用户名
                        Text(
                            text = "用户名",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // 种子币余额
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "种子币",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "1000 种子币",
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
                
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 抽屉菜单项
                drawerItems.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = index == selectedDrawerItem,
                        onClick = {
                            selectedDrawerItem = index
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            text = when (selectedItem) {
                                0 -> "Seeding"
                                1 -> "Goal"
                                2 -> "Harvest"
                                else -> "Seeding"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "菜单"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    navigationItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItem == index,
                            onClick = { selectedItem = index },
                            icon = {
                                Icon(
                                    imageVector = if (selectedItem == index) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(text = item.title) }
                        )
                    }
                }
            },
            floatingActionButton = {
                if (selectedItem == 0) {  // 只在"播种"页面显示添加按钮
                    FloatingActionButton(
                        onClick = { /* 添加新行为 */ },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "添加行为",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        ) { paddingValues ->
            // 根据选中的导航项显示不同的内容
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (selectedItem) {
                    0 -> PlantScreen()  // 播种页面
                    1 -> TargetScreen() // 目标页面
                    2 -> HarvestScreen() // 收获页面
                    else -> if (selectedDrawerItem in drawerItems.indices) {
                        drawerItems[selectedDrawerItem].screen()
                    }
                }
            }
        }
    }
}

/**
 * 播种屏幕（原首页）
 */
@Composable
fun PlantScreen() {
    Text(
        text = "播种 - 这里将显示行为记录和种植进度",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
}

/**
 * 目标屏幕
 */
@Composable
fun TargetScreen() {
    Text(
        text = "目标 - 这里将显示所有目标和进度",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
}

/**
 * 收获屏幕（数据分析）
 */
@Composable
fun HarvestScreen() {
    Text(
        text = "收获 - 这里将显示数据分析和统计",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
}

/**
 * 账户屏幕
 */
@Composable
fun AccountScreen() {
    Text(
        text = "账户 - 这里将显示用户信息和会员状态",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
}

/**
 * 插件商店屏幕
 */
@Composable
fun PluginStoreScreen() {
    Text(
        text = "插件商店 - 这里将显示可购买的插件",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
}

/**
 * 设置屏幕
 */
@Composable
fun SettingsScreen() {
    Text(
        text = "设置 - 这里将显示应用设置选项",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
}

/**
 * 底部导航项数据类
 */
data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * 抽屉菜单项数据类
 */
data class DrawerItem(
    val title: String,
    val icon: ImageVector,
    val screen: @Composable () -> Unit
) 