package com.example.seeding.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.seeding.data.local.entity.SeedEntity
import kotlinx.coroutines.flow.Flow

/**
 * 种子表数据访问对象
 */
@Dao
interface SeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeeds(seeds: List<SeedEntity>)
    
    @Query("SELECT * FROM seeds ORDER BY id ASC")
    fun getAllSeeds(): Flow<List<SeedEntity>>
    
    @Query("SELECT * FROM seeds WHERE id = :seedId")
    suspend fun getSeedById(seedId: Int): SeedEntity?
    
    @Query("SELECT COUNT(*) FROM seeds")
    suspend fun getSeedCount(): Int
} 