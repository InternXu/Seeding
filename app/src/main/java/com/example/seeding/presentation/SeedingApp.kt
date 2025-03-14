package com.example.seeding.presentation

// 需要在app/build.gradle.kts中添加:
// implementation("androidx.compose.material:material-icons-extended:1.6.0")

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
// 导入扩展图标库中的图标 - 图标从Icons中获取
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.outlined.Grass
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.util.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.abs
// 导入animateItemPlacement所需的包
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.delay
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.layout
import androidx.compose.ui.zIndex
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.collectAsState
import com.example.seeding.presentation.viewmodel.TargetViewModel
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester

// 使用自定义NumberPicker组件
// 模拟目标数据 - 定义为顶层变量以便不同的组合函数可以访问
// val mockTargets = mutableStateListOf<TargetItem>()

// 种子类型列表
val seedTypes = listOf(
    "保护生命（生命）", 
    "尊重他人财物（财富）", 
    "尊重他人伴侣关系（婚姻）", 
    "诚言实语（诚实）", 
    "和谐言语（和谐）", 
    "柔和言语（柔和）", 
    "有意义语言（达意）", 
    "随喜他人成功（随喜）", 
    "同情他人不幸（同情）", 
    "维持正确的世界观（正念）"
)

/**
 * 应用程序的主Compose组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeedingApp(targetViewModel: TargetViewModel) {
    // 使用rememberSaveable保存状态，以便屏幕旋转后保留
    var selectedItem by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(0) }
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // 目标添加对话框
    var showAddTargetDialog by remember { mutableStateOf(false) }
    
    // 添加全局编辑模式状态
    var isEditMode by remember { mutableStateOf(false) }
    
    val navigationItems = listOf(
        NavigationItem("播种", Icons.Default.Eco, "Seeding"),
        NavigationItem("目标", Icons.Default.EmojiEvents, "Goal"),
        NavigationItem("收获", Icons.Default.Grass, "Harvest")
    )
    
    // 抽屉菜单项
    val drawerItems = listOf(
        DrawerItem("用户信息", Icons.Default.Person, 0),
        DrawerItem("插件商店", Icons.Default.Info, 1),
        DrawerItem("设置", Icons.Default.Settings, 2)
    )
    
    // 当前页面标题
    val currentTitle = navigationItems[selectedItem].topBarTitle
    
    // 使用ModalNavigationDrawer包装整个界面
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp), // 设置为屏幕宽度的三分之一左右
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Spacer(Modifier.height(24.dp)) // 给顶部状态栏留出空间
                
                // 用户信息区域
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "用户头像",
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .padding(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "用户名",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(8.dp))
                
                // 抽屉菜单项
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.label) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        },
        gesturesEnabled = true // 允许手势操作
    ) {
        // 主界面的Scaffold
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(currentTitle) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "菜单"
                            )
                        }
                    },
                    // 添加在目标页面右上角显示的编辑按钮
                    actions = {
                        if (selectedItem == 1) { // 只在目标页面显示
                            IconButton(onClick = {
                                isEditMode = !isEditMode
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "编辑模式",
                                    tint = if (isEditMode) 
                                        MaterialTheme.colorScheme.primary 
                                    else 
                                        MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    navigationItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selectedItem == index,
                            onClick = { 
                                selectedItem = index
                                // 当离开目标页面时退出编辑模式
                                if (selectedItem != 1 && isEditMode) {
                                    isEditMode = false
                                }
                            }
                        )
                    }
                }
            },
            floatingActionButton = {
                // 只在"播种"和"目标"页面显示FAB
                if (selectedItem == 0) {
                    FloatingActionButton(
                        onClick = { 
                            // 处理播种页面的添加动作
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "添加")
                    }
                } else if (selectedItem == 1) {
                    FloatingActionButton(
                        onClick = {
                            // 显示添加目标对话框
                            showAddTargetDialog = true
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "添加目标")
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedItem) {
                    0 -> SowingScreen()
                    1 -> TargetScreen(isEditMode = isEditMode, targetViewModel = targetViewModel)
                    2 -> HarvestScreen()
                }
            }
        }
    }
    
    // 显示添加目标对话框
    if (showAddTargetDialog) {
        AddTargetDialog(
            onDismiss = { showAddTargetDialog = false },
            onAdd = { newTarget ->
                val added = targetViewModel.addTarget(
                    title = newTarget.title,
                    description = newTarget.description,
                    deadline = newTarget.deadline,
                    seedType = newTarget.seedType
                )
                // 只有在成功添加的情况下才关闭对话框
                if (added) {
                    showAddTargetDialog = false
                }
            },
            targetViewModel = targetViewModel
        )
    }
}

/**
 * 播种页面（占位）
 */
@Composable
fun SowingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("播种页面开发中...")
    }
}

/**
 * 目标屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetScreen(
    isEditMode: Boolean,
    targetViewModel: TargetViewModel
) {
    // 从ViewModel收集状态
    val activeTargets = targetViewModel.activeTargets.collectAsState().value
    val completedTargets = targetViewModel.completedTargets.collectAsState().value
    val abandonedTargets = targetViewModel.abandonedTargets.collectAsState().value
    val deletedTargets = targetViewModel.deletedTargets.collectAsState().value
    val activeSeeds = targetViewModel.activeSeeds.collectAsState().value
    
    // 处理目标查看和编辑
    var selectedTarget by remember { mutableStateOf<TargetItem?>(null) }
    var showTargetDetailDialog by remember { mutableStateOf(false) }
    
    // 处理目标删除确认
    var targetToDelete by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var isPermanentDelete by remember { mutableStateOf(false) }
    
    // 处理恢复失败提示
    var showRestoreErrorDialog by remember { mutableStateOf(false) }
    var targetToRestore by remember { mutableStateOf<TargetItem?>(null) }
    
    // 目标添加对话框
    var showAddTargetDialog by remember { mutableStateOf(false) }
    
    // 构建类别列表：全部 + 活跃种子类型 + 已完成 + 已放弃 + 已删除
    val categories = remember(activeSeeds) {
        listOf("全部") + 
        activeSeeds.map { seedIndex -> 
            val fullName = seedTypes[seedIndex]
            val shortName = if (fullName.contains("（")) {
                val startIndex = fullName.indexOf("（") + 1
                val endIndex = fullName.indexOf("）")
                if (startIndex > 0 && endIndex > startIndex) {
                    fullName.substring(startIndex, endIndex)
                } else {
                    fullName
                }
            } else {
                fullName
            }
            shortName
        } + 
        listOf("已完成", "已放弃", "已删除")
    }
    
    var selectedCategory by remember { mutableStateOf("全部") }
    
    // 如果当前选中的类别不再存在于categories中，重置为"全部"
    if (!categories.contains(selectedCategory) && selectedCategory != "全部" && 
        selectedCategory != "已完成" && selectedCategory != "已放弃" && selectedCategory != "已删除") {
        selectedCategory = "全部"
    }
    
    // 滚动相关状态
    val lazyListState = rememberLazyListState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // 分类选择器
        CategorySelector(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 显示目标列表
        val filteredTargets = when(selectedCategory) {
            "全部" -> activeTargets
            "已完成" -> completedTargets
            "已放弃" -> abandonedTargets
            "已删除" -> deletedTargets
            else -> {
                // 处理种子类型筛选
                val seedIndex = seedTypes.indexOfFirst { seedType ->
                    val shortName = if (seedType.contains("（")) {
                        val startIndex = seedType.indexOf("（") + 1
                        val endIndex = seedType.indexOf("）")
                        if (startIndex > 0 && endIndex > startIndex) {
                            seedType.substring(startIndex, endIndex)
                        } else {
                            seedType
                        }
                    } else {
                        seedType
                    }
                    shortName == selectedCategory
                }
                
                if (seedIndex >= 0) {
                    activeTargets.filter { it.seedType == seedIndex }
                } else {
                    emptyList()
                }
            }
        }
        
        // 按orderIndex排序
        val sortedTargets = remember(filteredTargets) {
            filteredTargets.sortedBy { it.orderIndex }
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (sortedTargets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // 如果是"已删除"标签并且为空，显示不同提示
                        if (selectedCategory == "已删除") {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Gray.copy(alpha = 0.5f),
                                modifier = Modifier.size(80.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "暂无已删除目标",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray.copy(alpha = 0.7f)
                            )
                        } else {
                            // 不同类别的空状态提示
                            val emptyStateText = when(selectedCategory) {
                                "全部" -> "还没有目标呢！"
                                "已完成" -> "暂无已完成目标"
                                "已放弃" -> "暂无已放弃目标"
                                else -> "暂无${selectedCategory}类型的目标"
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = emptyStateText,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // 添加操作提示，半透明样式
                            Text(
                                text = "点击右下角的 + 按钮添加新目标",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center
                            )
                            
                            // 显示箭头指向右下角
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 64.dp, end = 64.dp),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    modifier = Modifier
                                        .size(48.dp)
                                        .rotate(315f) // 旋转45度指向右下角
                                )
                            }
                        }
                    }
                }
            } else {
                @OptIn(ExperimentalFoundationApi::class)
                LazyColumn(
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 120.dp), // 增加底部内边距，确保最后的项目可以完全滚动到视图中
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp) // 添加水平内边距
                ) {
                    itemsIndexed(
                        items = sortedTargets,
                        key = { _, target -> target.id }
                    ) { index, target ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItemPlacement(), // 平滑动画
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 1.dp,
                                pressedElevation = 4.dp,
                                hoveredElevation = 2.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedCategory == "已删除") 
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                                else 
                                    MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            ),
                            onClick = {
                                // 非编辑模式下，点击显示详情
                                if (!isEditMode && selectedCategory != "已删除") {
                                    selectedTarget = target
                                    showTargetDetailDialog = true
                                }
                            }
                        ) {
                            TargetCard(
                                item = target,
                                isEditMode = isEditMode && selectedCategory != "已删除", // 编辑模式下所有卡片都显示编辑控件
                                onDeleteClick = {
                                    if (selectedCategory == "已删除") {
                                        // 已删除的目标点击删除按钮表示永久删除
                                        targetToDelete = target.id
                                        isPermanentDelete = true
                                        showDeleteConfirmDialog = true
                                    } else {
                                        // 正常目标点击删除按钮表示移到回收站
                                        targetToDelete = target.id
                                        isPermanentDelete = false
                                        showDeleteConfirmDialog = true
                                    }
                                },
                                onRestoreClick = {
                                    // 尝试恢复目标，并检查是否成功（是否有标题冲突）
                                    val restored = targetViewModel.restoreTarget(target.id)
                                    if (!restored) {
                                        // 如果恢复失败（可能是标题冲突），显示提示
                                        // 这里简单处理，可以后续添加一个Toast或Dialog提示用户
                                        showRestoreErrorDialog = true
                                        targetToRestore = target
                                    }
                                },
                                showRestoreOption = selectedCategory == "已删除",
                                // 添加上移和下移功能，但已删除的目标不能排序
                                enableMoveUp = isEditMode && selectedCategory != "已删除" && index > 0,
                                enableMoveDown = isEditMode && selectedCategory != "已删除" && index < sortedTargets.size - 1,
                                onMoveUp = {
                                    if (index > 0) {
                                        val currentItemId = sortedTargets[index].id
                                        val prevItemId = sortedTargets[index - 1].id
                                        targetViewModel.swapTargetsOrder(currentItemId, prevItemId)
                                    }
                                },
                                onMoveDown = {
                                    if (index < sortedTargets.size - 1) {
                                        val currentItemId = sortedTargets[index].id
                                        val nextItemId = sortedTargets[index + 1].id
                                        targetViewModel.swapTargetsOrder(currentItemId, nextItemId)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // 显示目标详情对话框
    if (showTargetDetailDialog && selectedTarget != null) {
        TargetDetailDialog(
            target = selectedTarget!!,
            onDismiss = {
                showTargetDetailDialog = false
                selectedTarget = null
            },
            onSave = { updatedTarget ->
                targetViewModel.updateTarget(updatedTarget)
                showTargetDetailDialog = false
                selectedTarget = null
            },
            onDelete = { targetId ->
                targetViewModel.markAsDeleted(targetId)
                showTargetDetailDialog = false
                selectedTarget = null
            }
        )
    }
    
    // 显示添加目标对话框
    if (showAddTargetDialog) {
        AddTargetDialog(
            onDismiss = { showAddTargetDialog = false },
            onAdd = { newTarget ->
                val added = targetViewModel.addTarget(
                    title = newTarget.title,
                    description = newTarget.description,
                    deadline = newTarget.deadline,
                    seedType = newTarget.seedType
                )
                // 只有在成功添加的情况下才关闭对话框
                if (added) {
                    showAddTargetDialog = false
                }
            },
            targetViewModel = targetViewModel
        )
    }
    
    // 显示删除确认对话框
    if (showDeleteConfirmDialog && targetToDelete != null) {
        DeleteConfirmDialog(
            onDismiss = { 
                showDeleteConfirmDialog = false
                targetToDelete = null
            },
            onConfirm = {
                if (isPermanentDelete) {
                    // 永久删除
                    targetViewModel.permanentlyDeleteTarget(targetToDelete!!)
                } else {
                    // 标记为已删除
                    targetViewModel.markAsDeleted(targetToDelete!!)
                }
                showDeleteConfirmDialog = false
                targetToDelete = null
            },
            isPermanentDelete = isPermanentDelete
        )
    }
    
    // 显示恢复失败对话框
    if (showRestoreErrorDialog && targetToRestore != null) {
        RestoreErrorDialog(
            onDismiss = {
                showRestoreErrorDialog = false
                targetToRestore = null
            },
            target = targetToRestore!!
        )
    }
}

/**
 * 状态标签组件
 */
@Composable
fun StatusChip(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "进行中" -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
        "已完成" -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
        "已放弃" -> MaterialTheme.colorScheme.error to MaterialTheme.colorScheme.onError
        else -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface
    }
    
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

/**
 * 目标项数据类
 */
data class TargetItem(
    val id: String,
    val title: String,
    val description: String,
    val deadline: String,
    val progress: Int, // 这个字段会被逐渐弃用，仅保留向后兼容性
    val status: String,
    val seedType: Int,
    val orderIndex: Int,
    val isDeleted: Boolean = false, // 表示是否已删除
    val createTime: String = Calendar.getInstance().let { 
        String.format("%04d-%02d-%02d", it.get(Calendar.YEAR), it.get(Calendar.MONTH) + 1, it.get(Calendar.DAY_OF_MONTH)) 
    } // 创建时间，默认为当前日期
)

/**
 * 收获页面（占位）
 */
@Composable
fun HarvestScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("收获页面开发中...")
    }
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
    val label: String,
    val icon: ImageVector,
    val topBarTitle: String
)

/**
 * 抽屉菜单项数据类
 */
data class DrawerItem(
    val label: String,
    val icon: ImageVector,
    val index: Int
)

/**
 * 类别选择器
 */
@Composable
fun CategorySelector(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    // 使用LazyRow实现横向滑动
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(end = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = when(category) {
                        "已完成" -> MaterialTheme.colorScheme.tertiaryContainer
                        "已放弃" -> MaterialTheme.colorScheme.errorContainer
                        "已删除" -> Color.Gray.copy(alpha = 0.3f)
                        else -> MaterialTheme.colorScheme.primaryContainer
                    },
                    selectedLabelColor = when(category) {
                        "已完成" -> MaterialTheme.colorScheme.onTertiaryContainer
                        "已放弃" -> MaterialTheme.colorScheme.onErrorContainer
                        "已删除" -> Color.DarkGray
                        else -> MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            )
        }
    }
}

/**
 * 添加目标对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTargetDialog(
    onDismiss: () -> Unit,
    onAdd: (TargetItem) -> Unit,
    targetViewModel: TargetViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showTitleError by remember { mutableStateOf(false) }
    
    // 日期选择
    var year by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var month by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) } // 月份从0开始
    var day by remember { mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) }
    
    // 显示日期选择器
    var showDatePicker by remember { mutableStateOf(false) }
    
    // 种子选择 - 不再设置默认选项
    var selectedSeedType by remember { mutableStateOf<Int?>(null) }
    var expandedSeedDropdown by remember { mutableStateOf(false) }
    
    // 格式化日期为字符串
    val formattedDate = remember(year, month, day) {
        String.format("%04d-%02d-%02d", year, month, day)
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加新目标") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 标题输入
                OutlinedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        if (showTitleError) showTitleError = false
                    },
                    label = { Text("目标标题") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = showTitleError,
                    supportingText = {
                        if (showTitleError) {
                            Text(
                                text = "此目标名称已存在，请更换",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                
                // 描述输入
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("描述") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
                
                // 截止日期选择
                Column {
                    Text("截止日期:")
                    Button(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(formattedDate)
                    }
                }
                
                // 种子类型选择
                Column {
                    Text("种子类型: ${selectedSeedType?.let { seedTypes[it] } ?: "请选择"}")
                    Button(
                        onClick = { expandedSeedDropdown = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(selectedSeedType?.let { seedTypes[it] } ?: "选择种子类型")
                    }
                    
                    if (expandedSeedDropdown) {
                        AlertDialog(
                            onDismissRequest = { expandedSeedDropdown = false },
                            title = { Text("选择种子类型") },
                            text = {
                                Column {
                                    seedTypes.forEachIndexed { index, seed ->
                                        Button(
                                            onClick = {
                                                selectedSeedType = index
                                                expandedSeedDropdown = false
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (selectedSeedType == index)
                                                    MaterialTheme.colorScheme.primaryContainer
                                                else
                                                    MaterialTheme.colorScheme.surface
                                            )
                                        ) {
                                            Text(
                                                text = seed,
                                                color = if (selectedSeedType == index)
                                                    MaterialTheme.colorScheme.onPrimaryContainer
                                                else
                                                    MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            },
                            confirmButton = {},
                            dismissButton = {
                                TextButton(onClick = { expandedSeedDropdown = false }) {
                                    Text("取消")
                                }
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && selectedSeedType != null) {
                        // 创建新目标
                        val newTarget = TargetItem(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            deadline = formattedDate,
                            progress = 0,
                            status = "进行中",
                            seedType = selectedSeedType!!,
                            orderIndex = 0 // 这里使用默认值，在ViewModel中会设置正确的值
                        )
                        
                        // 检查标题是否已存在
                        if (targetViewModel.isTitleExists(title)) {
                            showTitleError = true
                        } else {
                            // 标题不存在，添加新目标
                            onAdd(newTarget)
                            onDismiss() // 关闭对话框
                        }
                    }
                },
                enabled = title.isNotBlank() && selectedSeedType != null
            ) {
                Text("添加")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("取消")
            }
        }
    )
    
    // 日期选择对话框
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { selectedYear, selectedMonth, selectedDay ->
                year = selectedYear
                month = selectedMonth
                day = selectedDay
                showDatePicker = false
            },
            initialYear = year,
            initialMonth = month,
            initialDay = day
        )
    }
}

/**
 * 日期选择对话框
 */
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Int, Int, Int) -> Unit,
    initialYear: Int,
    initialMonth: Int,
    initialDay: Int
) {
    var selectedYear by remember { mutableStateOf(initialYear) }
    var selectedMonth by remember { mutableStateOf(initialMonth) }
    var selectedDay by remember { mutableStateOf(initialDay) }
    
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("选择日期") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 年份选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("年份:")
                    NumberPicker(
                        value = selectedYear,
                        onValueChange = { selectedYear = it },
                        range = (Calendar.getInstance().get(Calendar.YEAR))..2100
                    )
                }
                
                // 月份选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("月份:")
                    NumberPicker(
                        value = selectedMonth,
                        onValueChange = { selectedMonth = it },
                        range = 1..12
                    )
                }
                
                // 日期选择
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("日期:")
                    // 根据年和月计算该月的天数
                    val calendar = Calendar.getInstance()
                    calendar.set(selectedYear, selectedMonth - 1, 1) // 月份从0开始
                    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                    
                    NumberPicker(
                        value = selectedDay.coerceIn(1, daysInMonth),
                        onValueChange = { selectedDay = it },
                        range = 1..daysInMonth
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onDateSelected(selectedYear, selectedMonth, selectedDay) }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("取消")
            }
        }
    )
}

/**
 * 数字选择器组件
 */
@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange
) {
    Row(
        modifier = Modifier
            .height(48.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                if (value > range.first) {
                    onValueChange(value - 1)
                }
            },
            enabled = value > range.first
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "减少"
            )
        }
        
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .width(50.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 4.dp),
            textAlign = TextAlign.Center
        )
        
        IconButton(
            onClick = {
                if (value < range.last) {
                    onValueChange(value + 1)
                }
            },
            enabled = value < range.last
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "增加"
            )
        }
    }
}

/**
 * 目标详情对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetDetailDialog(
    target: TargetItem,
    onDismiss: () -> Unit,
    onSave: (TargetItem) -> Unit,
    onDelete: (String) -> Unit
) {
    var title by remember { mutableStateOf(target.title) }
    var description by remember { mutableStateOf(target.description) }
    
    // 删除确认对话框的状态
    var showDeleteConfirm by remember { mutableStateOf(false) }
    
    // 解析日期
    val dateParts = target.deadline.split("-").map { it.toIntOrNull() ?: 0 }
    var year by remember { mutableStateOf(if (dateParts.size > 0) dateParts[0] else 2023) }
    var month by remember { mutableStateOf(if (dateParts.size > 1) dateParts[1] else 1) }
    var day by remember { mutableStateOf(if (dateParts.size > 2) dateParts[2] else 1) }
    
    // 显示日期选择器
    var showDatePicker by remember { mutableStateOf(false) }
    
    // 不再手动设置进度，而是根据时间自动计算
    var status by remember { mutableStateOf(target.status) }
    
    // 计算进度和检查是否逾期
    val (progress, isOverdue) = calculateProgressAndOverdue(target.createTime, target.deadline)
    
    // 格式化日期为字符串
    val formattedDate = remember(year, month, day) {
        String.format("%04d-%02d-%02d", year, month, day)
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("目标详情") },
        text = {
            // 使用LazyColumn替换Column，启用滚动功能
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // 标题输入
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("目标标题") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                
                item {
                    // 描述输入
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("目标描述") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
                
                item {
                    // 日期选择器触发
                    OutlinedTextField(
                        value = formattedDate,
                        onValueChange = { },
                        label = { Text("截止日期") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "选择日期"
                                )
                            }
                        }
                    )
                }
                
                item {
                    // 创建时间（只读显示）
                    OutlinedTextField(
                        value = target.createTime,
                        onValueChange = { },
                        label = { Text("创建日期") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )
                }
                
                item {
                    // 种子类型（不可修改，只显示）
                    OutlinedTextField(
                        value = seedTypes[target.seedType],
                        onValueChange = { },
                        label = { Text("种子类型") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )
                }
                
                item {
                    // 替换进度滑块为时间进度显示
                    Column {
                        Text("时间进度:")
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        if (isOverdue && status == "进行中") {
                            // 显示逾期警告
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.errorContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "此目标已逾期！请更新截止日期或更改状态。",
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        } else if (status == "进行中") {
                            // 显示时间进度
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                LinearProgressIndicator(
                                    progress = { progress / 100f },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(8.dp),
                                    strokeCap = StrokeCap.Round
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${progress}%",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            
                            // 添加时间说明
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "从 ${target.createTime} 到 $formattedDate",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                item {
                    // 状态选择 - 水平滚动确保不会换行
                    Text("状态:")
                    ScrollableRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("进行中", "已完成", "已放弃").forEach { statusOption ->
                                FilterChip(
                                    selected = status == statusOption,
                                    onClick = { status = statusOption },
                                    label = { Text(statusOption) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = when(statusOption) {
                                            "进行中" -> MaterialTheme.colorScheme.primaryContainer
                                            "已完成" -> MaterialTheme.colorScheme.tertiaryContainer
                                            "已放弃" -> MaterialTheme.colorScheme.errorContainer
                                            else -> MaterialTheme.colorScheme.primaryContainer
                                        },
                                        selectedLabelColor = when(statusOption) {
                                            "进行中" -> MaterialTheme.colorScheme.onPrimaryContainer
                                            "已完成" -> MaterialTheme.colorScheme.onTertiaryContainer
                                            "已放弃" -> MaterialTheme.colorScheme.onErrorContainer
                                            else -> MaterialTheme.colorScheme.onPrimaryContainer
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
                
                item {
                    // 删除按钮
                    TextButton(
                        onClick = { showDeleteConfirm = true }, // 显示确认对话框
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "删除目标",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("删除目标")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        val updatedTarget = TargetItem(
                            id = target.id,
                            title = title,
                            description = description,
                            deadline = formattedDate,
                            progress = target.progress, // 保留原来的progress字段，但不再使用它
                            status = status,
                            seedType = target.seedType,
                            orderIndex = target.orderIndex,
                            isDeleted = target.isDeleted,
                            createTime = target.createTime // 保持创建时间不变
                        )
                        onSave(updatedTarget)
                    }
                }
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
    
    // 日期选择器弹窗
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { selectedYear, selectedMonth, selectedDay ->
                year = selectedYear
                month = selectedMonth
                day = selectedDay
                showDatePicker = false
            },
            initialYear = year,
            initialMonth = month,
            initialDay = day
        )
    }
    
    // 删除确认对话框
    if (showDeleteConfirm) {
        DeleteConfirmDialog(
            onDismiss = { showDeleteConfirm = false },
            onConfirm = { 
                onDelete(target.id)
                showDeleteConfirm = false
            },
            isPermanentDelete = false
        )
    }
}

/**
 * 可滚动的行布局
 */
@Composable
fun ScrollableRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        LazyRow {
            item { content() }
        }
    }
}

/**
 * 删除目标确认对话框
 */
@Composable
fun DeleteConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isPermanentDelete: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isPermanentDelete) "永久删除目标" else "删除目标") },
        text = { 
            Column {
                Text(
                    if (isPermanentDelete) 
                        "此操作将永久删除该目标，删除后将无法恢复！" 
                    else 
                        "确定要删除该目标吗？"
                )
                
                if (!isPermanentDelete) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "提示：删除后可以在\"已删除\"标签中恢复",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isPermanentDelete) "永久删除" else "确认删除")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

/**
 * 目标卡片内容组件
 */
@Composable
fun TargetCard(
    item: TargetItem,
    isEditMode: Boolean,
    onDeleteClick: () -> Unit,
    onRestoreClick: () -> Unit,
    showRestoreOption: Boolean = false,
    enableMoveUp: Boolean = false,
    enableMoveDown: Boolean = false,
    onMoveUp: () -> Unit = {},
    onMoveDown: () -> Unit = {}
) {
    // 计算进度和检查是否逾期
    val (progress, isOverdue) = calculateProgressAndOverdue(item.createTime, item.deadline)
    
    if (showRestoreOption) {
        // 已删除项的特殊显示方式
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题和删除时间
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                
                // 显示"已删除"标签
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Gray.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "已删除",
                        color = Color.DarkGray,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 种子类型
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = seedTypes[item.seedType],
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // 截止日期
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "截止日期: ${item.deadline}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 恢复和永久删除按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onRestoreClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Restore,
                        contentDescription = "恢复",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("恢复")
                }
                
                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "永久删除",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("永久删除")
                }
            }
        }
    } else if (isEditMode) {
        // 编辑模式下显示 - 简化为只显示标题、状态、上下箭头和删除按钮
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // 标题和状态行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 标题
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                
                // 状态
                StatusChip(status = item.status)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 排序和删除按钮行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 上移按钮
                IconButton(
                    onClick = onMoveUp,
                    enabled = enableMoveUp,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "上移",
                        tint = if (enableMoveUp) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
                
                // 下移按钮
                IconButton(
                    onClick = onMoveDown,
                    enabled = enableMoveDown,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "下移",
                        tint = if (enableMoveDown) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
                
                // 弹性空间
                Spacer(modifier = Modifier.weight(1f))
                
                // 删除按钮
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    } else {
        // 正常显示 - 完整信息
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                
                StatusChip(status = item.status)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 种子类型
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = seedTypes[item.seedType],
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // 截止日期
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "截止日期: ${item.deadline}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 进度条或逾期提示
            if (isOverdue && item.status == "进行中") {
                // 显示逾期提示
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "已逾期",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else if (item.status == "进行中") {
                // 显示进度条
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp),
                        strokeCap = StrokeCap.Round
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${progress}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 根据创建时间和截止时间计算进度，并判断是否逾期
 * @return Pair<Int, Boolean> 第一个值是进度百分比(0-100)，第二个值表示是否逾期
 */
fun calculateProgressAndOverdue(createTimeStr: String, deadlineStr: String): Pair<Int, Boolean> {
    try {
        // 解析日期字符串
        val createDate = parseDate(createTimeStr)
        val deadlineDate = parseDate(deadlineStr)
        val currentDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        
        // 检查是否逾期
        val isOverdue = currentDate.after(deadlineDate)
        
        // 如果已逾期，进度为100%
        if (isOverdue) {
            return Pair(100, true)
        }
        
        // 计算总时间跨度（毫秒）
        val totalTimeSpan = deadlineDate.time - createDate.time
        
        // 如果总时间跨度为0（创建日期和截止日期相同），进度为50%
        if (totalTimeSpan <= 0) {
            return Pair(50, false)
        }
        
        // 计算已经过去的时间（毫秒）
        val elapsedTime = currentDate.time - createDate.time
        
        // 计算进度百分比
        val progressPercentage = ((elapsedTime.toDouble() / totalTimeSpan.toDouble()) * 100).toInt().coerceIn(0, 100)
        
        return Pair(progressPercentage, false)
    } catch (e: Exception) {
        // 如果日期解析出错，返回默认值
        return Pair(0, false)
    }
}

/**
 * 解析日期字符串为Date对象
 */
fun parseDate(dateStr: String): Date {
    val parts = dateStr.split("-")
    if (parts.size != 3) throw IllegalArgumentException("Invalid date format: $dateStr")
    
    val year = parts[0].toInt()
    val month = parts[1].toInt() - 1 // Calendar月份从0开始
    val day = parts[2].toInt()
    
    return Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
}

/**
 * 恢复失败提示对话框
 */
@Composable
fun RestoreErrorDialog(
    onDismiss: () -> Unit,
    target: TargetItem
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("无法恢复目标") },
        text = { 
            Text("无法恢复\"${target.title}\"，因为已存在同名的目标。请先删除或重命名现有目标后再尝试恢复。")
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("我知道了")
            }
        }
    )
} 