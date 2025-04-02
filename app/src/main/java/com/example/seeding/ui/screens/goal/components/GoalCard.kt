package com.example.seeding.ui.screens.goal.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.seeding.R
import com.example.seeding.data.model.Goal
import com.example.seeding.data.model.GoalStatus
import com.example.seeding.util.DateUtils
import com.example.seeding.util.SeedUtils

@Composable
fun GoalCard(
    goal: Goal,
    onGoalClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onRestoreClick: () -> Unit,
    onPermanentDeleteClick: () -> Unit,
    isEditMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isActive = goal.status == GoalStatus.IN_PROGRESS || goal.status == GoalStatus.OVERDUE
    val isCompleted = goal.status == GoalStatus.COMPLETED || goal.status == GoalStatus.OVERDUE_COMPLETED
    val isDeleted = goal.status == GoalStatus.ABANDONED
    
    val isOverdue = DateUtils.isOverdue(goal.deadline) && isActive
    
    // 计算进度百分比
    val totalDuration = goal.deadline - goal.createdAt
    val elapsedTime = System.currentTimeMillis() - goal.createdAt
    var progress = (elapsedTime.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f)
    
    // 如果已完成，进度为100%
    if (isCompleted) progress = 1f
    
    // 进度条动画
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "ProgressAnimation"
    )
    
    // 使用非常简单的卡片，避免任何边框问题
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isDeleted -> MaterialTheme.colorScheme.surfaceVariant
                isCompleted -> Color(0xFFE0F2E9)  // 浅绿色背景，不使用alpha值
                isOverdue -> Color(0xFFFBE9E7)  // 浅红色背景，不使用alpha值
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isActive) { onGoalClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题 - 不添加任何状态标签
            Text(
                text = goal.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 描述
            if (goal.description.isNotEmpty()) {
                Text(
                    text = goal.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // 相关种子
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    goal.seedIds.forEach { seedId ->
                        SeedChip(seedId = seedId)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 时间信息 - 对于已删除的卡片不显示时间信息
            if (!isDeleted) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${stringResource(R.string.created_time)}: ${DateUtils.formatDate(goal.createdAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = "${stringResource(R.string.deadline)}: ${DateUtils.formatDate(goal.deadline)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // 进度条 - 对于已删除的卡片不显示进度条
                Column {
                    LinearProgressIndicator(
                        progress = animatedProgress,
                        color = when {
                            isCompleted -> MaterialTheme.colorScheme.secondary
                            isOverdue -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.primary
                        },
                        trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // 剩余时间或已逾期 - 始终显示在右侧
                    if (!isCompleted) {
                        Text(
                            text = if (isOverdue) 
                                stringResource(R.string.overdue) 
                            else 
                                stringResource(R.string.remaining_time, DateUtils.getRemainingTime(goal.deadline)),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                // 为已删除卡片添加额外的间距
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // 操作按钮
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                when {
                    isDeleted -> {
                        // 已删除的目标显示恢复和彻底删除按钮
                        FilledTonalButton(
                            onClick = onRestoreClick,
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restore,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.restore_goal))
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Button(
                            onClick = onPermanentDeleteClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.permanent_delete))
                        }
                    }
                    isCompleted -> {
                        // 已完成的目标只显示删除按钮
                        Button(
                            onClick = onDeleteClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.delete_goal))
                        }
                    }
                    else -> {
                        // 活动状态的目标
                        FilledTonalButton(
                            onClick = onCompleteClick,
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.complete))
                        }
                        
                        // 仅在编辑模式下显示删除按钮
                        if (isEditMode) {
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Button(
                                onClick = onDeleteClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.error
                                ),
                                modifier = Modifier.wrapContentSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(R.string.delete_goal))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SeedChip(
    seedId: Int,
    modifier: Modifier = Modifier
) {
    // 使用单一普通颜色
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = modifier
    ) {
        Text(
            text = stringResource(SeedUtils.getSeedFullNameResId(seedId)),
            color = contentColor,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
} 