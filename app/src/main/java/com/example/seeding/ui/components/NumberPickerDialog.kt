package com.example.seeding.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.seeding.R
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showMaterial3DatePicker(
    initialDate: Long = System.currentTimeMillis(),
    onDismissRequest: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    // 使用Material 3的日期选择器状态
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate)
    
    // 显示Material 3风格的日期选择器对话框
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { timestamp ->
                        // 设置为当天的23:59:59
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = timestamp
                            set(Calendar.HOUR_OF_DAY, 23)
                            set(Calendar.MINUTE, 59)
                            set(Calendar.SECOND, 59)
                        }
                        onDateSelected(calendar.timeInMillis)
                    }
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
} 