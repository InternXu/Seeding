package com.example.seeding.ui.screens.action.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.seeding.R
import com.example.seeding.data.model.Action
import com.example.seeding.data.model.ActionType
import com.example.seeding.data.model.Commitment
import com.example.seeding.data.model.CommitmentStatus
import com.example.seeding.data.model.CommitmentTimeFrames
import com.example.seeding.data.model.Goal
import com.example.seeding.ui.screens.action.ActionDialogMode
import com.example.seeding.ui.screens.goal.components.SeedChip
import com.example.seeding.util.DateUtils
import com.example.seeding.util.SeedUtils
import com.example.seeding.ui.screens.action.components.SeedSelectChip
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActionDialog(
    mode: ActionDialogMode,
    action: Action? = null,
    commitmentContent: String? = null,
    availableGoals: List<Goal> = emptyList(),
    onDismiss: () -> Unit,
    onConfirm: (content: String, type: ActionType, seedIds: List<Int>, goalIds: List<String>) -> Unit = { _, _, _, _ -> },
    onFulfillCommitment: (String) -> Unit = { _ -> }
) {
    val shouldShowDialog = action != null || mode == ActionDialogMode.ADD
    
    if (shouldShowDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 5.dp,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp)
            ) {
                if (mode == ActionDialogMode.ADD) {
                    AddActionContent(
                        availableGoals = availableGoals,
                        onDismiss = onDismiss,
                        onConfirm = onConfirm,
                        onAddCommitment = { _, _ -> }  // 不需要实现，会在添加行为后单独调用
                    )
                } else {
                    action?.let {
                        ViewActionContent(
                            action = it,
                            commitmentContent = commitmentContent,
                            availableGoals = availableGoals,
                            onDismiss = onDismiss,
                            onFulfillCommitment = { onFulfillCommitment(it) }
                        )
                    }
                } 
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ViewActionContent(
    action: Action,
    commitmentContent: String?,
    availableGoals: List<Goal>,
    onDismiss: () -> Unit,
    onFulfillCommitment: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 标题栏
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.action_detail),
                style = MaterialTheme.typography.headlineSmall
            )
            
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 类型标签
        val typeText = when(action.type) {
            ActionType.POSITIVE -> stringResource(R.string.positive)
            ActionType.NEGATIVE -> stringResource(R.string.negative)
            ActionType.NEUTRAL -> stringResource(R.string.neutral_action)
        }
        
        Surface(
            color = when(action.type) {
                ActionType.POSITIVE -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ActionType.NEGATIVE -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                ActionType.NEUTRAL -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
            },
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = typeText,
                style = MaterialTheme.typography.bodyMedium,
                color = when(action.type) {
                    ActionType.POSITIVE -> MaterialTheme.colorScheme.primary
                    ActionType.NEGATIVE -> MaterialTheme.colorScheme.error
                    ActionType.NEUTRAL -> MaterialTheme.colorScheme.secondary
                },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 行为内容
        Text(
            text = stringResource(R.string.action_content),
            style = MaterialTheme.typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = action.content,
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 创建时间
        Text(
            text = stringResource(R.string.creation_time),
            style = MaterialTheme.typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = DateUtils.formatDateTime(action.timestamp),
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 相关种子
        Text(
            text = stringResource(R.string.related_seeds),
            style = MaterialTheme.typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            action.seedIds.forEach { seedId ->
                SeedChip(seedId = seedId, useFullName = true)
            }
        }
        
        // 如果有关联目标，显示目标
        if (action.goalIds.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(R.string.related_goals),
                style = MaterialTheme.typography.titleSmall
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            action.goalIds.forEach { goalId ->
                // 查找目标名称
                val goalTitle = availableGoals.find { it.goalId == goalId }?.title ?: "未知目标"
                
                Text(
                    text = "• $goalTitle",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        
        // 如果有承诺，显示承诺内容
        if (commitmentContent != null) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Spacer(modifier = Modifier.height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.outlineVariant))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.commitment),
                    style = MaterialTheme.typography.titleSmall
                )
                
                // 显示承诺状态
                val actionCommitment = action.getCommitment()
                val statusText = when (actionCommitment?.status) {
                    CommitmentStatus.PENDING -> stringResource(id = R.string.commitment_pending)
                    CommitmentStatus.FULFILLED -> stringResource(id = R.string.commitment_fulfilled)
                    CommitmentStatus.EXPIRED -> stringResource(id = R.string.commitment_expired)
                    CommitmentStatus.UNFULFILLED -> stringResource(id = R.string.commitment_unfulfilled)
                    else -> ""
                }
                
                val statusColor = when (actionCommitment?.status) {
                    CommitmentStatus.PENDING -> Color(0xFF2196F3) // 蓝色
                    CommitmentStatus.FULFILLED -> Color(0xFF4CAF50) // 绿色
                    else -> Color(0xFFF44336) // 红色
                }
                
                if (statusText.isNotEmpty()) {
                    Surface(
                        color = statusColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.bodySmall,
                            color = statusColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 显示承诺内容
            Text(
                text = commitmentContent,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 显示承诺期限和截止时间
            val actionCommitment = action.getCommitment()
            if (actionCommitment != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = CommitmentTimeFrames.getTimeFrameName(actionCommitment.timeFrame),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = DateUtils.formatDateTime(actionCommitment.deadline),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 添加完成承诺按钮，仅当状态为PENDING或EXPIRED但不超过12小时时显示
                val currentTime = System.currentTimeMillis()
                val isWithin12Hours = currentTime - actionCommitment.deadline < TimeUnit.HOURS.toMillis(12)
                val canFulfill = actionCommitment.status == CommitmentStatus.PENDING || 
                                 (actionCommitment.status == CommitmentStatus.UNFULFILLED && isWithin12Hours)
                
                if (canFulfill) {
                    Button(
                        onClick = { onFulfillCommitment(actionCommitment.commitmentId) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.commitment_completed),
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AddActionContent(
    availableGoals: List<Goal>,
    onDismiss: () -> Unit,
    onConfirm: (content: String, type: ActionType, seedIds: List<Int>, goalIds: List<String>) -> Unit,
    onAddCommitment: (content: String, timeFrame: Int) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var isNegativeAction by remember { mutableStateOf(false) }
    val selectedSeedIds = remember { mutableStateListOf<Int>() }
    val selectedGoalIds = remember { mutableStateListOf<String>() }
    
    // 过滤后的目标列表
    var filteredGoals by remember { mutableStateOf(availableGoals) }
    var showGoalSelection by remember { mutableStateOf(false) }
    
    // 验证状态
    var contentError by remember { mutableStateOf(false) }
    var seedError by remember { mutableStateOf(false) }
    
    // 获取actionType
    val actionType by remember(isNegativeAction) {
        derivedStateOf { if (isNegativeAction) ActionType.NEGATIVE else ActionType.POSITIVE }
    }
    
    val backgroundColor = if (isNegativeAction) 
        MaterialTheme.colorScheme.error.copy(alpha = 0.08f) 
        else MaterialTheme.colorScheme.surface
    
    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 标题栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.add_action),
                    style = MaterialTheme.typography.headlineSmall
                )
                
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cancel))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 行为内容
            OutlinedTextField(
                value = content,
                onValueChange = { 
                    content = it 
                    contentError = it.isBlank()
                },
                label = { Text("我刚刚做了...") },
                isError = contentError,
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
            
            if (contentError) {
                Text(
                    text = "请输入行为内容",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 行为类型选择 - 改用开关
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isNegativeAction) stringResource(R.string.negative_action) else stringResource(R.string.positive_action),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isNegativeAction) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
                
                Switch(
                    checked = isNegativeAction,
                    onCheckedChange = { 
                        isNegativeAction = it
                        // 切换为负面行为时，清空已选择的目标
                        if (it) {
                            selectedGoalIds.clear()
                            showGoalSelection = false
                        } else if (selectedSeedIds.isNotEmpty()) {
                            // 切换为正面行为时，如果已有种子，则显示目标选择
                            filteredGoals = availableGoals.filter { goal ->
                                goal.seedIds.any { it in selectedSeedIds }
                            }
                            showGoalSelection = filteredGoals.isNotEmpty()
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.error,
                        checkedTrackColor = MaterialTheme.colorScheme.errorContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 选择种子
            Text(
                text = stringResource(R.string.select_seeds),
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SeedUtils.getAllSeedModels().forEach { seed ->
                    val isSelected = selectedSeedIds.contains(seed.id)
                    
                    SeedSelectChip(
                        seedId = seed.id,
                        isSelected = isSelected,
                        useFullName = true,
                        onClick = {
                            if (isSelected) {
                                selectedSeedIds.remove(seed.id)
                            } else {
                                selectedSeedIds.add(seed.id)
                            }
                            seedError = selectedSeedIds.isEmpty()
                            
                            // 当选择了种子而且是正面行为时，过滤相关目标
                            if (!isNegativeAction && selectedSeedIds.isNotEmpty()) {
                                filteredGoals = availableGoals.filter { goal ->
                                    goal.seedIds.any { it in selectedSeedIds }
                                }
                                showGoalSelection = true
                            } else {
                                showGoalSelection = false
                            }
                        }
                    )
                }
            }
            
            if (seedError) {
                Text(
                    text = "请至少选择一个种子",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }
            
            // 正面行为且有选择种子时，显示目标选择
            if (!isNegativeAction && showGoalSelection && filteredGoals.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = stringResource(R.string.select_goals),
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    filteredGoals.forEach { goal ->
                        val isSelected = selectedGoalIds.contains(goal.goalId)
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.secondaryContainer 
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable {
                                    if (isSelected) {
                                        selectedGoalIds.remove(goal.goalId)
                                    } else {
                                        selectedGoalIds.add(goal.goalId)
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = goal.title,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = if (isSelected) 
                                    MaterialTheme.colorScheme.onSecondaryContainer 
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 底部按钮
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = {
                        // 验证输入
                        val isContentValid = content.isNotBlank()
                        val isSeedValid = selectedSeedIds.isNotEmpty()
                        
                        contentError = !isContentValid
                        seedError = !isSeedValid
                        
                        if (isContentValid && isSeedValid) {
                            // 负面行为不关联目标，确保传递空列表
                            val goalIdsToSubmit = if (isNegativeAction) emptyList() else selectedGoalIds.toList()
                            onConfirm(content, actionType, selectedSeedIds.toList(), goalIdsToSubmit)
                        }
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
        }
    }
}

// 添加扩展函数让Action获取关联的Commitment
private fun Action.getCommitment(): Commitment? {
    return if (this.hasCommitment) {
        (this.tag as? Commitment)
    } else {
        null
    }
}