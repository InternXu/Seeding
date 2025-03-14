package com.example.seeding

import android.app.Application
import com.example.seeding.data.database.SeedingDatabase
import com.example.seeding.data.repository.TargetRepository

/**
 * 应用入口类 - 负责初始化全局组件
 */
class SeedingApplication : Application() {
    
    // 数据库实例
    lateinit var database: SeedingDatabase
        private set
    
    // 目标仓库
    lateinit var targetRepository: TargetRepository
        private set
    
    override fun onCreate() {
        super.onCreate()
        
        // 初始化数据库
        database = SeedingDatabase.getDatabase(this)
        
        // 初始化仓库
        targetRepository = TargetRepository(database.targetDao())
    }
} 