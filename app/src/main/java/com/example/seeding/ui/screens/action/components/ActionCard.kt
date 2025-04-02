package com.example.seeding.ui.screens.action.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.seeding.R
import com.example.seeding.data.model.Action
import com.example.seeding.data.model.ActionType
import com.example.seeding.data.model.CommitmentStatus
import com.example.seeding.data.model.Goal
import com.example.seeding.util.DateUtils
import com.example.seeding.util.SeedUtils

/**
 * 行为卡片
 */
@Composable
fun ActionCard(
    action: Action,
    commitmentStatus: CommitmentStatus?,
    onClick: () -> Unit,
    availableGoals: List<Goal> = emptyList(),
    modifier: Modifier = Modifier
) {
    // 根据行为类型确定卡片背景颜色
    val cardBackgroundColor = when(action.type) {
        ActionType.POSITIVE -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ActionType.NEGATIVE -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        ActionType.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    Box(modifier = modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            // 使用0dp的elevation去除阴影效果
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // 行为内容
                Text(
                    text = action.content,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 相关种子显示 - 使用Compose环境友好的方式
                if (action.seedIds.isNotEmpty()) {
                    // 创建获取种子名称文本的Composable组件
                    SeedNamesText(
                        seedIds = action.seedIds,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                // 正面行为才显示关联目标 - 去掉星号图标
                if (action.type == ActionType.POSITIVE && action.goalIds.isNotEmpty()) {
                    val goalNames = action.goalIds.mapNotNull { goalId ->
                        availableGoals.find { it.goalId == goalId }?.title
                    }
                    
                    if (goalNames.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = goalNames.joinToString(", "),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                
                // 添加时间
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 显示时间
                    Text(
                        text = DateUtils.formatDateTime(action.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // 如果有承诺，显示承诺状态
                    if (action.hasCommitment && commitmentStatus != null) {
                        val statusText = when (commitmentStatus) {
                            CommitmentStatus.PENDING -> stringResource(id = R.string.commitment_pending)
                            CommitmentStatus.FULFILLED -> stringResource(id = R.string.commitment_fulfilled_full)
                            CommitmentStatus.EXPIRED -> stringResource(id = R.string.commitment_expired)
                            CommitmentStatus.UNFULFILLED -> stringResource(id = R.string.commitment_unfulfilled_full)
                        }
                        
                        val statusColor = when (commitmentStatus) {
                            CommitmentStatus.PENDING -> Color(0xFF2196F3) // 蓝色
                            CommitmentStatus.FULFILLED -> Color(0xFF4CAF50) // 绿色
                            CommitmentStatus.EXPIRED -> Color(0xFFF44336) // 红色
                            CommitmentStatus.UNFULFILLED -> Color(0xFFF44336) // 红色
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (commitmentStatus == CommitmentStatus.UNFULFILLED) FontWeight.Bold else FontWeight.Normal,
                            color = statusColor
                        )
                    }
                }
            }
        }
    }
}

/**
 * 种子名称文本组件 - 解决在joinToString中调用stringResource的问题
 */
@Composable
fun SeedNamesText(
    seedIds: List<Int>,
    style: androidx.compose.ui.text.TextStyle,
    color: Color
) {
    val names = seedIds.map { seedId ->
        stringResource(SeedUtils.getSeedFullNameResId(seedId))
    }
    
    Text(
        text = names.joinToString(", "),
        style = style,
        color = color
    )
}