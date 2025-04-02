package com.example.seeding.ui.screens.goal.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.seeding.R
import com.example.seeding.data.model.Goal
import com.example.seeding.ui.components.showMaterial3DatePicker
import com.example.seeding.ui.screens.goal.GoalDialogMode
import com.example.seeding.util.DateUtils
import com.example.seeding.util.SeedUtils
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GoalDialog(
    mode: GoalDialogMode,
    goal: Goal?,
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String, deadline: Long, seedIds: List<Int>) -> Unit,
    onDelete: (() -> Unit)? = null // 添加删除回调
) {
    val isEditMode = mode == GoalDialogMode.EDIT
    val titleText = if (isEditMode) R.string.edit_goal else R.string.new_goal
    
    // 状态
    var title by remember { mutableStateOf(if (isEditMode) goal?.title ?: "" else "") }
    var description by remember { mutableStateOf(if (isEditMode) goal?.description ?: "" else "") }
    var deadline by remember { mutableStateOf(if (isEditMode) goal?.deadline ?: getTomorrowDate() else getTomorrowDate()) }
    val selectedSeedIds = remember { mutableStateListOf<Int>().apply {
        if (isEditMode && goal != null) {
            addAll(goal.seedIds)
        }
    }}
    
    // 验证状态
    var titleError by remember { mutableStateOf(false) }
    var deadlineError by remember { mutableStateOf(false) }
    var seedError by remember { mutableStateOf(false) }
    
    // 日期选择器状态
    var showDatePicker by remember { mutableStateOf(false) }
    
    // 对话框显示
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
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
                        text = stringResource(titleText),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cancel))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 目标名称
                OutlinedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        titleError = it.isBlank()
                    },
                    label = { Text(stringResource(R.string.goal_name)) },
                    isError = titleError,
                    supportingText = { 
                        if (titleError) Text(stringResource(R.string.goal_name_required)) 
                    },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isEditMode // 编辑模式下不可修改标题
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 目标详情
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.goal_detail)) },
                    minLines = 3,
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 截止日期
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .border(
                            width = 1.dp,
                            color = if (deadlineError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.small
                        )
                        .clickable { showDatePicker = true }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${stringResource(R.string.deadline)}: ${DateUtils.formatDate(deadline)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = stringResource(R.string.deadline),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (deadlineError) {
                    Text(
                        text = stringResource(R.string.deadline_required),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 种子选择
                Text(
                    text = stringResource(R.string.seed_selection),
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isEditMode) {
                        // 编辑模式下只显示已选择的种子
                        selectedSeedIds.forEach { seedId ->
                            SeedSelectChip(
                                seedId = seedId,
                                isSelected = true,
                                enabled = false
                            )
                        }
                    } else {
                        // 添加模式下显示所有种子
                        SeedUtils.getAllSeedIds().forEach { seedId ->
                            val isSelected = selectedSeedIds.contains(seedId)
                            SeedSelectChip(
                                seedId = seedId,
                                isSelected = isSelected,
                                enabled = true,
                                onClick = {
                                    if (isSelected) {
                                        selectedSeedIds.remove(seedId)
                                    } else {
                                        selectedSeedIds.add(seedId)
                                    }
                                    seedError = selectedSeedIds.isEmpty()
                                }
                            )
                        }
                    }
                }
                
                if (seedError) {
                    Text(
                        text = stringResource(R.string.goal_seed_required),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 底部按钮行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 删除按钮 - 仅在编辑模式显示
                    if (isEditMode && onDelete != null) {
                        Button(
                            onClick = onDelete,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(stringResource(R.string.delete))
                        }
                    } else {
                        Spacer(modifier = Modifier.width(0.dp))
                    }
                    
                    // 确认按钮
                    Button(
                        onClick = {
                            // 验证输入
                            val isTitleValid = title.isNotBlank()
                            val isDeadlineValid = deadline > 0
                            val isSeedValid = selectedSeedIds.isNotEmpty()
                            
                            titleError = !isTitleValid
                            deadlineError = !isDeadlineValid
                            seedError = !isSeedValid
                            
                            if (isTitleValid && isDeadlineValid && isSeedValid) {
                                onConfirm(title, description, deadline, selectedSeedIds.toList())
                            }
                        }
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
    
    // 日期选择器
    if (showDatePicker) {
        showMaterial3DatePicker(
            initialDate = deadline,
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { selectedDate ->
                deadline = selectedDate
                deadlineError = false
                showDatePicker = false
            }
        )
    }
}

@Composable
fun SeedSelectChip(
    seedId: Int,
    isSelected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    // 使用统一的颜色而不是种子特定的颜色
    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceVariant
    
    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onSurfaceVariant
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(SeedUtils.getSeedFullNameResId(seedId)),  // 使用全名
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// 获取明天的日期
private fun getTomorrowDate(): Long {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    return calendar.timeInMillis
} 