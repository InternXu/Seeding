package com.example.seeding.ui.screens.action.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.seeding.util.SeedUtils

/**
 * 种子芯片组件 - 用于显示种子名称的小标签
 */
@Composable
fun SeedChip(
    seedId: Int,
    modifier: Modifier = Modifier,
    useFullName: Boolean = false
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
            text = if (useFullName) {
                stringResource(SeedUtils.getSeedFullNameResId(seedId))
            } else {
                stringResource(SeedUtils.getSeedNameResId(seedId))
            },
            color = contentColor,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * 可选择的种子芯片组件 - 用于选择种子
 */
@Composable
fun SeedSelectChip(
    seedId: Int,
    isSelected: Boolean,
    useFullName: Boolean = false,
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
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // 使用stringResource获取种子名称
        Text(
            text = stringResource(SeedUtils.getSeedFullNameResId(seedId)),
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
} 