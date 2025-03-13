package com.example.seeding.domain.repository

import com.example.seeding.data.local.entity.SeedCategory
import com.example.seeding.data.local.entity.SeedEntity
import com.example.seeding.domain.model.Resource
import kotlinx.coroutines.flow.Flow

/**
 * 种子仓库接口
 */
interface SeedRepository {
    
    /**
     * 获取所有种子
     */
    fun getAllSeeds(): Flow<Resource<List<SeedEntity>>>
    
    /**
     * 根据ID获取种子
     */
    suspend fun getSeedById(seedId: Int): Resource<SeedEntity>
    
    /**
     * 根据类别获取种子
     */
    fun getSeedsByCategory(category: SeedCategory): Flow<Resource<List<SeedEntity>>>
    
    /**
     * 根据父种子ID获取子种子
     */
    fun getChildSeeds(parentId: Int): Flow<Resource<List<SeedEntity>>>
    
    /**
     * 根据ID列表获取种子
     */
    suspend fun getSeedsByIds(seedIds: List<Int>): Resource<List<SeedEntity>>
    
    /**
     * 搜索种子
     */
    fun searchSeeds(query: String): Flow<Resource<List<SeedEntity>>>
    
    /**
     * 刷新种子数据
     */
    suspend fun refreshSeeds(): Resource<Unit>
} 