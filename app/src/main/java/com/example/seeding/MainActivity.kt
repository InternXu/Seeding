package com.example.seeding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.seeding.presentation.SeedingApp
import com.example.seeding.presentation.viewmodel.TargetViewModel
import com.example.seeding.ui.theme.SeedingTheme

/**
 * 应用主Activity
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 初始化ViewModel
        val targetViewModel = ViewModelProvider(this)[TargetViewModel::class.java]
        
        // 初始化数据，但不添加示例数据
        targetViewModel.initializeData()
        
        setContent {
            SeedingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SeedingApp(targetViewModel = targetViewModel)
                }
            }
        }
    }
} 