package com.example.seeding.ui.screens.goal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.seeding.R
import com.example.seeding.ui.screens.goal.GoalFilter
import com.example.seeding.util.SeedUtils

@Composable
fun FilterTabs(
    selectedFilter: GoalFilter,
    onFilterSelected: (GoalFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // "全部"标签固定在最左侧
        FilterTab(
            text = stringResource(R.string.all_goals),
            isSelected = selectedFilter == GoalFilter.ALL,
            onClick = { onFilterSelected(GoalFilter.ALL) },
            modifier = Modifier.zIndex(1f) // 确保在最上层
        )
        
        // 种子标签 - 放在可滚动区域
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(scrollState)
        ) {
            // 种子标签 - 所有种子使用相同颜色
            SeedUtils.getAllSeedIds().forEach { seedId ->
                FilterTab(
                    text = stringResource(SeedUtils.getSeedNameResId(seedId)),
                    isSelected = selectedFilter is GoalFilter.BySeed && 
                                (selectedFilter as GoalFilter.BySeed).seedId == seedId,
                    onClick = { onFilterSelected(GoalFilter.BySeed(seedId)) }
                )
            }
        }
        
        // "已完成"和"已删除"标签固定在最右侧
        FilterTab(
            text = stringResource(R.string.completed_goals),
            isSelected = selectedFilter == GoalFilter.COMPLETED,
            onClick = { onFilterSelected(GoalFilter.COMPLETED) },
            modifier = Modifier.zIndex(1f) // 确保在最上层
        )
        
        FilterTab(
            text = stringResource(R.string.deleted_goals),
            isSelected = selectedFilter == GoalFilter.DELETED,
            onClick = { onFilterSelected(GoalFilter.DELETED) },
            modifier = Modifier.zIndex(1f) // 确保在最上层
        )
    }
}

@Composable
private fun FilterTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 根据文本内容设置特殊颜色
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        text == stringResource(R.string.all_goals) -> Color(0xFFF0F0F0) // 浅灰色背景（全部）
        text == stringResource(R.string.completed_goals) -> Color(0xFFD6F5D6) // 浅绿色背景
        text == stringResource(R.string.deleted_goals) -> Color(0xFFF8D7D7) // 浅红色背景
        else -> MaterialTheme.colorScheme.surface
    }
    
    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        text == stringResource(R.string.all_goals) -> Color(0xFF505050) // 深灰色文字（全部）
        text == stringResource(R.string.completed_goals) -> Color(0xFF0A6E0A) // 深绿色文字
        text == stringResource(R.string.deleted_goals) -> Color(0xFF9C1A1A) // 深红色文字
        else -> MaterialTheme.colorScheme.onSurface
    }
    
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
} 