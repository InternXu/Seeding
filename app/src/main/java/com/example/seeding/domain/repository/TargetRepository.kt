package com.example.seeding.domain.repository

import com.example.seeding.data.local.entity.TargetEntity
import com.example.seeding.data.local.entity.TargetProgressEntity
import com.example.seeding.data.local.entity.TargetStatus
import com.example.seeding.data.local.entity.TargetWithProgress
import com.example.seeding.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * 目标仓库接口
 */
interface TargetRepository {
    
    /**
     * 创建目标
     */
    suspend fun createTarget(target: TargetEntity, progressPoints: List<TargetProgressEntity>? = null): Resource<TargetEntity>
    
    /**
     * 更新目标
     */
    suspend fun updateTarget(target: TargetEntity): Resource<TargetEntity>
    
    /**
     * 删除目标
     */
    suspend fun deleteTarget(targetId: String): Resource<Unit>
    
    /**
     * 根据ID获取目标
     */
    suspend fun getTargetById(targetId: String): Resource<TargetEntity>
    
    /**
     * 根据ID获取目标及其进度
     */
    suspend fun getTargetWithProgressById(targetId: String): Resource<TargetWithProgress>
    
    /**
     * 获取用户的所有目标
     */
    fun getTargetsByUserId(userId: String): Flow<Resource<List<TargetEntity>>>
    
    /**
     * 获取用户的所有目标及其进度
     */
    fun getTargetsWithProgressByUserId(userId: String): Flow<Resource<List<TargetWithProgress>>>
    
    /**
     * 获取用户的活跃目标
     */
    fun getTargetsByStatus(userId: String, status: TargetStatus): Flow<Resource<List<TargetEntity>>>
    
    /**
     * 获取用户的活跃目标及其进度
     */
    fun getTargetsWithProgressByStatus(userId: String, status: TargetStatus): Flow<Resource<List<TargetWithProgress>>>
    
    /**
     * 获取用户的承诺
     */
    fun getPromisesByStatus(userId: String, status: TargetStatus): Flow<Resource<List<TargetEntity>>>
    
    /**
     * 获取即将到期的目标
     */
    fun getUpcomingTargets(userId: String, status: TargetStatus, startDate: LocalDateTime, endDate: LocalDateTime): Flow<Resource<List<TargetEntity>>>
    
    /**
     * 更新目标状态
     */
    suspend fun updateTargetStatus(targetId: String, status: TargetStatus): Resource<Unit>
    
    /**
     * 添加目标进度
     */
    suspend fun addTargetProgress(progress: TargetProgressEntity): Resource<TargetProgressEntity>
    
    /**
     * 更新目标进度
     */
    suspend fun updateTargetProgress(progress: TargetProgressEntity): Resource<TargetProgressEntity>
    
    /**
     * 删除目标进度
     */
    suspend fun deleteTargetProgress(progressId: String): Resource<Unit>
    
    /**
     * 根据目标ID获取所有进度
     */
    fun getProgressByTargetId(targetId: String): Flow<Resource<List<TargetProgressEntity>>>
    
    /**
     * 更新目标进度完成状态
     */
    suspend fun updateProgressCompletionStatus(progressId: String, isCompleted: Boolean, completedAt: LocalDateTime?): Resource<Unit>
    
    /**
     * 同步目标数据
     */
    suspend fun syncTargets(): Resource<Unit>
} 