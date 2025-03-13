package com.example.seeding.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.seeding.data.local.entity.BehaviorEntity
import com.example.seeding.data.local.entity.BehaviorImpact
import com.example.seeding.data.local.entity.BehaviorTargetCrossRef
import com.example.seeding.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * 行为数据访问对象接口
 */
@Dao
interface BehaviorDao {
    
    /**
     * 插入行为
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehavior(behavior: BehaviorEntity): Long
    
    /**
     * 更新行为
     */
    @Update
    suspend fun updateBehavior(behavior: BehaviorEntity)
    
    /**
     * 删除行为
     */
    @Delete
    suspend fun deleteBehavior(behavior: BehaviorEntity)
    
    /**
     * 插入行为与目标的关联
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehaviorTargetCrossRef(crossRef: BehaviorTargetCrossRef)
    
    /**
     * 批量插入行为与目标的关联
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehaviorTargetCrossRefs(crossRefs: List<BehaviorTargetCrossRef>)
    
    /**
     * 根据ID获取行为
     */
    @Query("SELECT * FROM behaviors WHERE behaviorId = :behaviorId")
    suspend fun getBehaviorById(behaviorId: String): BehaviorEntity?
    
    /**
     * 获取用户的所有行为
     */
    @Query("SELECT * FROM behaviors WHERE userId = :userId ORDER BY performedAt DESC")
    fun getBehaviorsByUserId(userId: String): Flow<List<BehaviorEntity>>
    
    /**
     * 获取用户的正面/负面行为
     */
    @Query("SELECT * FROM behaviors WHERE userId = :userId AND isPositive = :isPositive ORDER BY performedAt DESC")
    fun getBehaviorsByType(userId: String, isPositive: Boolean): Flow<List<BehaviorEntity>>
    
    /**
     * 获取与特定种子相关的行为
     */
    @Query("SELECT * FROM behaviors WHERE userId = :userId AND :seedId IN (SELECT value FROM json_each(seedIds)) ORDER BY performedAt DESC")
    fun getBehaviorsBySeedId(userId: String, seedId: Int): Flow<List<BehaviorEntity>>
    
    /**
     * 获取与特定目标相关的行为
     */
    @Query("SELECT b.* FROM behaviors b JOIN behavior_target_cross_ref r ON b.behaviorId = r.behaviorId WHERE b.userId = :userId AND r.targetId = :targetId ORDER BY b.performedAt DESC")
    fun getBehaviorsByTargetId(userId: String, targetId: String): Flow<List<BehaviorEntity>>
    
    /**
     * 获取与特定目标相关的行为，按影响类型筛选
     */
    @Query("SELECT b.* FROM behaviors b JOIN behavior_target_cross_ref r ON b.behaviorId = r.behaviorId WHERE b.userId = :userId AND r.targetId = :targetId AND r.impact = :impact ORDER BY b.performedAt DESC")
    fun getBehaviorsByTargetIdAndImpact(userId: String, targetId: String, impact: BehaviorImpact): Flow<List<BehaviorEntity>>
    
    /**
     * 获取特定时间范围内的行为
     */
    @Query("SELECT * FROM behaviors WHERE userId = :userId AND performedAt BETWEEN :startDate AND :endDate ORDER BY performedAt DESC")
    fun getBehaviorsByDateRange(userId: String, startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<BehaviorEntity>>
    
    /**
     * 获取待同步的行为
     */
    @Query("SELECT * FROM behaviors WHERE syncStatus = :status")
    suspend fun getBehaviorsByStatus(status: SyncStatus): List<BehaviorEntity>
    
    /**
     * 更新行为同步状态
     */
    @Query("UPDATE behaviors SET syncStatus = :status WHERE behaviorId = :behaviorId")
    suspend fun updateBehaviorSyncStatus(behaviorId: String, status: SyncStatus)
    
    /**
     * 获取待同步的行为目标关联
     */
    @Query("SELECT * FROM behavior_target_cross_ref WHERE syncStatus = :status")
    suspend fun getBehaviorTargetCrossRefsByStatus(status: SyncStatus): List<BehaviorTargetCrossRef>
    
    /**
     * 更新行为目标关联同步状态
     */
    @Query("UPDATE behavior_target_cross_ref SET syncStatus = :status WHERE behaviorId = :behaviorId AND targetId = :targetId")
    suspend fun updateBehaviorTargetCrossRefSyncStatus(behaviorId: String, targetId: String, status: SyncStatus)
    
    /**
     * 搜索行为
     */
    @Query("SELECT * FROM behaviors WHERE userId = :userId AND content LIKE '%' || :query || '%' ORDER BY performedAt DESC")
    fun searchBehaviors(userId: String, query: String): Flow<List<BehaviorEntity>>
} 