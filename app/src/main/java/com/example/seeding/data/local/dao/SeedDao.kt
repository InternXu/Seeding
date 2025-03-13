package com.example.seeding.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seeding.data.local.entity.SeedCategory
import com.example.seeding.data.local.entity.SeedEntity
import kotlinx.coroutines.flow.Flow

/**
 * 种子数据访问对象接口
 */
@Dao
interface SeedDao {
    
    /**
     * 插入种子
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeed(seed: SeedEntity)
    
    /**
     * 批量插入种子
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeeds(seeds: List<SeedEntity>)
    
    /**
     * 更新种子
     */
    @Update
    suspend fun updateSeed(seed: SeedEntity)
    
    /**
     * 根据ID获取种子
     */
    @Query("SELECT * FROM seeds WHERE seedId = :seedId")
    suspend fun getSeedById(seedId: Int): SeedEntity?
    
    /**
     * 获取所有种子
     */
    @Query("SELECT * FROM seeds WHERE isActive = 1")
    fun getAllSeeds(): Flow<List<SeedEntity>>
    
    /**
     * 根据类别获取种子
     */
    @Query("SELECT * FROM seeds WHERE category = :category AND isActive = 1")
    fun getSeedsByCategory(category: SeedCategory): Flow<List<SeedEntity>>
    
    /**
     * 根据父种子ID获取子种子
     */
    @Query("SELECT * FROM seeds WHERE parentSeedId = :parentId AND isActive = 1")
    fun getChildSeeds(parentId: Int): Flow<List<SeedEntity>>
    
    /**
     * 根据ID列表获取种子
     */
    @Query("SELECT * FROM seeds WHERE seedId IN (:seedIds) AND isActive = 1")
    suspend fun getSeedsByIds(seedIds: List<Int>): List<SeedEntity>
    
    /**
     * 搜索种子
     */
    @Query("SELECT * FROM seeds WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' AND isActive = 1")
    fun searchSeeds(query: String): Flow<List<SeedEntity>>
} 