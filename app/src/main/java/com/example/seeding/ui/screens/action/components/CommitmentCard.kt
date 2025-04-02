package com.example.seeding.ui.screens.action.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.seeding.R
import com.example.seeding.data.model.Commitment
import com.example.seeding.data.model.CommitmentStatus
import com.example.seeding.util.DateUtils
import com.example.seeding.util.SeedUtils
import com.example.seeding.ui.screens.action.components.SeedNamesText
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun CommitmentCard(
    commitment: Commitment,
    onFulfill: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 状态更新定时器
    var currentTimeMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    
    // 每秒更新一次当前时间，使倒计时精确到秒
    LaunchedEffect(Unit) {
        while(true) {
            currentTimeMillis = System.currentTimeMillis()
            delay(1000) // 1秒刷新一次
        }
    }
    
    // 计算剩余时间
    val remainingTime = commitment.deadline - currentTimeMillis
    val isExpired = remainingTime <= 0
    
    // 计算进度百分比
    val totalDuration = commitment.deadline - commitment.timestamp
    val elapsedTime = currentTimeMillis - commitment.timestamp
    val progress = ((totalDuration - elapsedTime).toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f)
    
    // 进度条动画
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "CommitmentProgressAnimation"
    )
    
    // 倒计时文本 - 精确到秒
    val timeRemainingText = if (isExpired) {
        stringResource(R.string.commitment_expired)
    } else {
        DateUtils.getRemainingTimeWithSecondsComposable(currentTimeMillis, commitment.deadline)
    }
    
    // 浅绿色背景，无边框设计
    val backgroundColor = Color(0xFFE8F5E9) // 浅绿色
    val progressColor = Color(0xFF4CAF50) // 绿色
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        // 使用0dp的elevation去除阴影效果
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 承诺内容
            Text(
                text = commitment.content,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 相关种子显示 - 使用Compose环境友好的方式
            if (commitment.seedIds.isNotEmpty()) {
                SeedNamesText(
                    seedIds = commitment.seedIds,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // 倒计时
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.remaining_time, timeRemainingText),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (isExpired) 
                        MaterialTheme.colorScheme.error 
                    else 
                        Color(0xFF2E7D32) // 深绿色
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 进度条 - 使用绿色主题
            if (!isExpired && commitment.status == CommitmentStatus.PENDING) {
                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier.fillMaxWidth(),
                    trackColor = progressColor.copy(alpha = 0.2f),
                    color = progressColor,
                    strokeCap = StrokeCap.Round
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // 美化完成按钮 - 固定在右下角
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                // 让所有承诺在截止时间后12小时内都能显示完成按钮
                val isWithin12Hours = currentTimeMillis - commitment.deadline < TimeUnit.HOURS.toMillis(12)
                
                // 对于PENDING状态或在12小时宽限期内的承诺，都显示完成按钮
                if (commitment.status == CommitmentStatus.PENDING || isWithin12Hours) {
                    Button(
                        onClick = onFulfill,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50), // 绿色
                            contentColor = Color.White
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