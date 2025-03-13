package com.example.seeding.domain.repository

import com.example.seeding.data.local.entity.BehaviorEntity
import com.example.seeding.data.local.entity.BehaviorImpact
import com.example.seeding.data.local.entity.BehaviorTargetCrossRef
import com.example.seeding.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * 行为仓库接口
 */
interface BehaviorRepository {
    
    /**
     * 创建行为
     */
    suspend fun createBehavior(behavior: BehaviorEntity, targetRelations: List<BehaviorTargetCrossRef>? = null): Resource<BehaviorEntity>
    
    /**
     * 更新行为
     */
    suspend fun updateBehavior(behavior: BehaviorEntity): Resource<BehaviorEntity>
    
    /**
     * 删除行为
     */
    suspend fun deleteBehavior(behaviorId: String): Resource<Unit>
    
    /**
     * 根据ID获取行为
     */
    suspend fun getBehaviorById(behaviorId: String): Resource<BehaviorEntity>
    
    /**
     * 获取用户的所有行为
     */
    fun getBehaviorsByUserId(userId: String): Flow<Resource<List<BehaviorEntity>>>
    
    /**
     * 获取用户的正面/负面行为
     */
    fun getBehaviorsByType(userId: String, isPositive: Boolean): Flow<Resource<List<BehaviorEntity>>>
    
    /**
     * 获取与特定种子相关的行为
     */
    fun getBehaviorsBySeedId(userId: String, seedId: Int): Flow<Resource<List<BehaviorEntity>>>
    
    /**
     * 获取与特定目标相关的行为
     */
    fun getBehaviorsByTargetId(userId: String, targetId: String): Flow<Resource<List<BehaviorEntity>>>
    
    /**
     * 获取与特定目标相关的行为，按影响类型筛选
     */
    fun getBehaviorsByTargetIdAndImpact(userId: String, targetId: String, impact: BehaviorImpact): Flow<Resource<List<BehaviorEntity>>>
    
    /**
     * 获取特定时间范围内的行为
     */
    fun getBehaviorsByDateRange(userId: String, startDate: LocalDateTime, endDate: LocalDateTime): Flow<Resource<List<BehaviorEntity>>>
    
    /**
     * 添加行为与目标的关联
     */
    suspend fun addBehaviorTargetRelation(relation: BehaviorTargetCrossRef): Resource<Unit>
    
    /**
     * 批量添加行为与目标的关联
     */
    suspend fun addBehaviorTargetRelations(relations: List<BehaviorTargetCrossRef>): Resource<Unit>
    
    /**
     * 搜索行为
     */
    fun searchBehaviors(userId: String, query: String): Flow<Resource<List<BehaviorEntity>>>
    
    /**
     * 同步行为数据
     */
    suspend fun syncBehaviors(): Resource<Unit>
} 