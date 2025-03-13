package com.example.seeding

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Seeding应用程序主入口点
 * 使用Hilt进行依赖注入
 */
@HiltAndroidApp
class SeedingApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // 初始化全局配置
    }
} 