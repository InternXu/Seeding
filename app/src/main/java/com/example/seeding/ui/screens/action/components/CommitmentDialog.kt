package com.example.seeding.ui.screens.action.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.seeding.R
import com.example.seeding.data.model.Action
import com.example.seeding.data.model.CommitmentTimeFrames

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommitmentDialog(
    action: Action,
    onDismiss: () -> Unit,
    onConfirm: (content: String, timeFrame: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var commitmentContent by remember { mutableStateOf("") }
    var selectedTimeFrame by remember { mutableStateOf(CommitmentTimeFrames.FIFTEEN_MINUTES) }
    var timeFrameExpanded by remember { mutableStateOf(false) }
    var contentError by remember { mutableStateOf(false) }
    
    // 时间选项列表
    val timeOptions = remember {
        listOf(
            CommitmentTimeFrames.THREE_MINUTES to CommitmentTimeFrames.getDisplayText(CommitmentTimeFrames.THREE_MINUTES),
            CommitmentTimeFrames.FIFTEEN_MINUTES to CommitmentTimeFrames.getDisplayText(CommitmentTimeFrames.FIFTEEN_MINUTES),
            CommitmentTimeFrames.THIRTY_MINUTES to CommitmentTimeFrames.getDisplayText(CommitmentTimeFrames.THIRTY_MINUTES),
            CommitmentTimeFrames.ONE_HOUR to CommitmentTimeFrames.getDisplayText(CommitmentTimeFrames.ONE_HOUR),
            CommitmentTimeFrames.TWO_HOURS to CommitmentTimeFrames.getDisplayText(CommitmentTimeFrames.TWO_HOURS),
            CommitmentTimeFrames.THREE_HOURS to CommitmentTimeFrames.getDisplayText(CommitmentTimeFrames.THREE_HOURS)
        )
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
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
                        text = stringResource(R.string.make_commitment),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close, 
                            contentDescription = stringResource(R.string.cancel),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 负面行为内容提示
                Surface(
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = stringResource(R.string.your_negative_action),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = action.content,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 时间框架选择标题
                Text(
                    text = stringResource(R.string.select_commitment_timeframe),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 时间框架下拉选择框
                ExposedDropdownMenuBox(
                    expanded = timeFrameExpanded,
                    onExpandedChange = { timeFrameExpanded = it }
                ) {
                    OutlinedTextField(
                        value = CommitmentTimeFrames.getDisplayTextComposable(selectedTimeFrame),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.select_time)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = timeFrameExpanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = timeFrameExpanded,
                        onDismissRequest = { timeFrameExpanded = false }
                    ) {
                        timeOptions.forEach { (timeFrame, displayText) ->
                            DropdownMenuItem(
                                text = { Text(CommitmentTimeFrames.getDisplayTextComposable(timeFrame)) },
                                onClick = {
                                    selectedTimeFrame = timeFrame
                                    timeFrameExpanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 承诺内容提示
                Text(
                    text = stringResource(R.string.commitment_prefix, CommitmentTimeFrames.getDisplayTextComposable(selectedTimeFrame)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 承诺内容输入
                OutlinedTextField(
                    value = commitmentContent,
                    onValueChange = { 
                        commitmentContent = it 
                        contentError = it.isBlank()
                    },
                    label = { Text(stringResource(R.string.commitment_content_hint)) },
                    isError = contentError,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )
                
                if (contentError) {
                    Text(
                        text = stringResource(R.string.please_enter_commitment),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 按钮区域
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text(
                            text = stringResource(R.string.cancel)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            if (commitmentContent.isNotBlank()) {
                                onConfirm(commitmentContent, selectedTimeFrame)
                            } else {
                                contentError = true
                            }
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
} 