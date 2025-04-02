package com.example.seeding.ui.screens.action

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.seeding.R
import com.example.seeding.data.model.Action
import com.example.seeding.data.model.Commitment
import com.example.seeding.data.model.CommitmentStatus
import com.example.seeding.ui.screens.action.components.ActionCard
import com.example.seeding.ui.screens.action.components.ActionDialog
import com.example.seeding.ui.screens.action.components.CommitmentCard
import com.example.seeding.ui.screens.action.components.CommitmentDialog
import com.example.seeding.ui.screens.action.components.SearchDialog
import com.example.seeding.util.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import timber.log.Timber
import com.example.seeding.ui.SearchEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionScreen(
    navController: NavController,
    viewModel: ActionViewModel = hiltViewModel()
) {
    val screenData by viewModel.combinedData.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    
    // 监听全局搜索事件流
    LaunchedEffect(Unit) {
        // 使用全局事件通道接收搜索事件
        SearchEvents.searchTrigger.collect { timestamp ->
            Timber.d("收到搜索事件，时间戳: $timestamp")
            viewModel.showSearchDialog()
        }
    }
    
    // 当创建了新的负面行为并需要添加承诺时显示承诺对话框
    var showCommitmentDialog by remember { mutableStateOf(false) }
    
    // 跟踪每个日期分组的展开状态
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    
    // 监听lastCreatedActionId变化，显示承诺对话框
    LaunchedEffect(uiState.lastCreatedActionId) {
        if (uiState.lastCreatedActionId != null) {
            showCommitmentDialog = true
        }
    }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddActionDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_action)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 8.dp,
                    bottom = innerPadding.calculateBottomPadding(),
                    start = innerPadding.calculateLeftPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateRightPadding(LayoutDirection.Ltr)
                )
        ) {
            // 错误信息
            screenData.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            // 当前时间
            val currentTime = System.currentTimeMillis()
            
            // 获取在倒计时中或逾期不超过12小时的承诺
            val activeCommitments = screenData.pendingCommitments.filter { commitment ->
                // 待履行的承诺显示
                commitment.status == CommitmentStatus.PENDING ||
                // 未履行或过期但不超过12小时的承诺也显示
                ((commitment.status == CommitmentStatus.UNFULFILLED || commitment.status == CommitmentStatus.EXPIRED) && 
                (currentTime - commitment.deadline < TimeUnit.HOURS.toMillis(12)))
            }.sortedBy { it.deadline }
            
            // 获取承诺对应的行为ID，这些行为在下面的行为列表中仍会显示，但会标记为已有承诺
            val activeCommitmentActionIds = activeCommitments.map { it.actionId }.toSet()
            
            // 按日期对行为进行分组
            val actionsByDate = screenData.actions.groupBy { action ->
                DateUtils.formatDate(action.timestamp)
            }.toSortedMap(compareByDescending { it }) // 按日期降序排序
            
            // 如果所有内容为空则显示空状态
            if (screenData.actions.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_actions),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 32.dp)
                )
            } else {
                // 显示承诺和行为列表
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 先显示活跃的承诺卡片（置顶）
                    if (activeCommitments.isNotEmpty()) {
                        items(activeCommitments) { commitment ->
                            CommitmentCard(
                                commitment = commitment,
                                onFulfill = { viewModel.fulfillCommitment(commitment.commitmentId) },
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                    
                    // 按日期分组显示行为
                    actionsByDate.forEach { (date, actions) ->
                        // 为每个日期初始化展开状态，如果未设置，默认为展开
                        val isExpanded = expandedStates.getOrPut(date) { true }
                        
                        // 日期标题和折叠按钮 - 简化为一条线
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                IconButton(
                                    onClick = { expandedStates[date] = !isExpanded },
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isExpanded) 
                                            Icons.Default.KeyboardArrowDown else 
                                            Icons.Default.KeyboardArrowRight,
                                        contentDescription = if (isExpanded) "收起" else "展开"
                                    )
                                }
                                
                                // 显示日期，格式化为更友好的形式
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val today = Date()
                                val yesterday = Date(today.time - 24 * 60 * 60 * 1000)
                                val todayStr = dateFormat.format(today)
                                val yesterdayStr = dateFormat.format(yesterday)
                                
                                val formattedDate = when (date) {
                                    todayStr -> stringResource(R.string.today)
                                    yesterdayStr -> stringResource(R.string.yesterday)
                                    else -> date
                                }
                                
                                Text(
                                    text = formattedDate,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Text(
                                    text = "(${actions.size})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                // 添加一条分隔线来代替原来的矩形背景
                                Divider(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                        
                        // 如果该日期分组是展开的，显示该日期下的所有行为卡片
                        if (isExpanded) {
                            items(actions) { action ->
                                // 查找关联的承诺（包括活跃的和非活跃的）
                                val commitmentStatus = if (action.hasCommitment) {
                                    // 首先在活跃承诺中查找
                                    screenData.pendingCommitments
                                        .find { it.actionId == action.actionId }
                                        ?.status
                                    // 如果没找到，在所有承诺中查找（可能是FULFILLED或UNFULFILLED状态）
                                    ?: screenData.allCommitments
                                        .find { it.actionId == action.actionId }
                                        ?.status
                                } else null
                                
                                ActionCard(
                                    action = action,
                                    commitmentStatus = commitmentStatus,
                                    availableGoals = uiState.availableGoals,
                                    onClick = { viewModel.showActionDetail(action) },
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // 添加搜索对话框
    SearchDialog(
        isVisible = uiState.isSearchActive,
        searchKeyword = uiState.searchKeyword,
        searchResults = uiState.searchResults,
        availableGoals = uiState.availableGoals,
        commitmentStatuses = screenData.pendingCommitments.associate { it.actionId to it.status },
        onDismiss = { viewModel.hideSearchDialog() },
        onSearch = { keyword -> 
            viewModel.updateSearchKeyword(keyword)
            viewModel.performSearch() 
        },
        onActionClick = { action -> 
            viewModel.showActionDetail(action) 
        }
    )
    
    // 添加行为对话框
    if (screenData.showDialog) {
        // 查找当前行为的承诺 - 包括所有状态的承诺，不仅仅是待履行
        val currentCommitment = remember(screenData.currentAction) {
            if (screenData.currentAction?.hasCommitment == true) {
                // 获取所有commitments，优先查找pendingCommitments
                screenData.pendingCommitments
                    .find { it.actionId == screenData.currentAction?.actionId }
                    ?: 
                // 如果在pendingCommitments中找不到（可能已完成或过期），也会在allCommitments中查找
                screenData.allCommitments
                    .find { it.actionId == screenData.currentAction?.actionId }
            } else null
        }
        
        // 将承诺添加到action中便于显示详情
        val actionWithCommitment = screenData.currentAction?.let { action ->
            if (action.hasCommitment && currentCommitment != null) {
                // 创建副本并设置tag
                action.copy().also { 
                    it.tag = currentCommitment 
                }
            } else {
                action
            }
        }
        
        ActionDialog(
            mode = screenData.dialogMode,
            action = actionWithCommitment,
            commitmentContent = currentCommitment?.content,
            availableGoals = uiState.availableGoals,
            onDismiss = { viewModel.hideActionDialog() },
            onConfirm = { content, type, seedIds, goalIds ->
                viewModel.addAction(content, type, seedIds, goalIds)
            },
            onFulfillCommitment = { commitmentId ->
                viewModel.fulfillCommitment(commitmentId)
                viewModel.hideActionDialog()
            }
        )
    }
    
    // 添加承诺对话框
    if (showCommitmentDialog && uiState.lastCreatedActionId != null) {
        val action = screenData.actions.find { it.actionId == uiState.lastCreatedActionId }
        if (action != null) {
            CommitmentDialog(
                action = action,
                onDismiss = {
                    showCommitmentDialog = false
                    // 重置lastCreatedActionId，避免重复显示
                    viewModel.resetLastCreatedActionId()
                },
                onConfirm = { content, timeFrame ->
                    viewModel.addCommitment(
                        actionId = uiState.lastCreatedActionId!!,
                        content = content,
                        seedIds = action.seedIds,
                        timeFrame = timeFrame
                    )
                    showCommitmentDialog = false
                }
            )
        }
    }
} 