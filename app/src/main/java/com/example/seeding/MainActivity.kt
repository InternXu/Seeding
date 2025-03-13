package com.example.seeding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.seeding.presentation.SeedingApp
import com.example.seeding.ui.theme.SeedingTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 应用程序的主Activity，作为Compose UI的容器
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeedingTheme {
                // 使用Material3主题包装整个应用
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SeedingApp()
                }
            }
        }
    }
} 