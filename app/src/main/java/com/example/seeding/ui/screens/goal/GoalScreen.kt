package com.example.seeding.ui.screens.goal

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.seeding.R
import com.example.seeding.data.model.Goal
import com.example.seeding.ui.screens.goal.components.FilterTabs
import com.example.seeding.ui.screens.goal.components.GoalCard
import com.example.seeding.ui.screens.goal.components.GoalDialog
import com.example.seeding.ui.screens.goal.dialogs.DeleteConfirmDialog
import com.example.seeding.ui.screens.goal.dialogs.PermanentDeleteConfirmDialog
import com.example.seeding.ui.screens.goal.dialogs.CompleteConfirmDialog
import com.example.seeding.util.SeedUtils
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalScreen(
    navController: NavController,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val goals by viewModel.filteredGoals.collectAsState()

    // 从savedStateHandle获取编辑模式状态
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val isEditMode by savedStateHandle?.getStateFlow("isEditMode", false)?.collectAsState() ?: remember { mutableStateOf(false) }
    
    // 当编辑模式变化时，同步更新ViewModel中的状态
    LaunchedEffect(isEditMode) {
        if (isEditMode != uiState.isEditMode) {
            viewModel.toggleEditMode()
        }
    }

    // 动态更新顶部标题栏标题 - 这部分会由SeedingApp中的顶部标题栏显示
    when (val filter = uiState.selectedFilter) {
        is GoalFilter.BySeed -> {
            // 如果需要，这里可以添加一些逻辑来更新标题，但目前不需要
        }
        else -> {
            // 其他过滤条件无需特殊处理
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddGoalDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_goal)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 0.dp,
                    bottom = innerPadding.calculateBottomPadding(), 
                    start = innerPadding.calculateLeftPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateRightPadding(LayoutDirection.Ltr)
                )
        ) {
            // 筛选标签栏 - 紧贴顶部
            FilterTabs(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = { viewModel.updateFilter(it) }
            )
            
            // 错误信息显示
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            // 目标列表 - 直接显示内容，不显示"我的目标"标题
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .animateContentSize()
            ) {
                if (goals.isEmpty()) {
                    // 空状态
                    Text(
                        text = stringResource(R.string.no_goals),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                            .align(Alignment.TopCenter)
                    )
                } else {
                    // 目标列表
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(goals) { goal ->
                            GoalCard(
                                goal = goal,
                                onGoalClick = { viewModel.showEditGoalDialog(goal) },
                                onCompleteClick = { viewModel.completeGoal(goal.goalId) },
                                onDeleteClick = { viewModel.showDeleteConfirmDialog(goal) },
                                onRestoreClick = { viewModel.restoreGoal(goal.goalId) },
                                onPermanentDeleteClick = { viewModel.showPermanentDeleteDialog(goal) },
                                isEditMode = isEditMode,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
    
    // 新增/编辑目标对话框
    if (uiState.showGoalDialog) {
        GoalDialog(
            mode = uiState.goalDialogMode,
            goal = uiState.currentGoal,
            onDismiss = { viewModel.hideGoalDialog() },
            onConfirm = { title, description, deadline, seedIds ->
                if (uiState.goalDialogMode == GoalDialogMode.ADD) {
                    viewModel.addGoal(title, description, deadline, seedIds)
                } else {
                    uiState.currentGoal?.let {
                        viewModel.updateGoal(it.goalId, description, deadline)
                    }
                }
            },
            // 添加删除回调，但仅在编辑模式下有效
            onDelete = if (uiState.goalDialogMode == GoalDialogMode.EDIT) {
                {
                    uiState.currentGoal?.let { 
                        viewModel.showDeleteConfirmDialog(it)
                    }
                }
            } else null
        )
    }
    
    // 删除确认对话框
    if (uiState.showDeleteConfirmDialog) {
        DeleteConfirmDialog(
            onDismiss = { viewModel.hideDeleteConfirmDialog() },
            onConfirm = { 
                uiState.currentGoal?.let { viewModel.deleteGoal(it.goalId) } 
            }
        )
    }
    
    // 彻底删除确认对话框
    if (uiState.showPermanentDeleteDialog) {
        PermanentDeleteConfirmDialog(
            onDismiss = { viewModel.hidePermanentDeleteDialog() },
            onConfirm = { 
                uiState.currentGoal?.let { viewModel.permanentDeleteGoal(it) } 
            }
        )
    }
    
    // 完成目标确认对话框
    if (uiState.showCompleteConfirmDialog) {
        CompleteConfirmDialog(
            onDismiss = { viewModel.hideCompleteConfirmDialog() },
            onConfirm = { viewModel.confirmCompleteGoal() }
        )
    }
} 