package com.example.seeding.domain.repository

import com.example.seeding.data.model.Seed
import kotlinx.coroutines.flow.Flow

/**
 * 种子仓库接口 - 领域层
 */
interface SeedRepository {
    /**
     * 获取所有种子
     */
    fun getAllSeeds(): Flow<List<Seed>>
    
    /**
     * 通过ID获取种子
     */
    suspend fun getSeedById(seedId: Int): Seed?
    
    /**
     * 初始化种子数据
     */
    suspend fun initializeSeeds()
    
    /**
     * 获取种子数量
     */
    suspend fun getSeedCount(): Int
} 