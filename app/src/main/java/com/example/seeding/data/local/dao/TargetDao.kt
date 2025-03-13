package com.example.seeding.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.seeding.data.local.entity.SyncStatus
import com.example.seeding.data.local.entity.TargetEntity
import com.example.seeding.data.local.entity.TargetProgressEntity
import com.example.seeding.data.local.entity.TargetStatus
import com.example.seeding.data.local.entity.TargetWithProgress
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * 目标数据访问对象接口
 */
@Dao
interface TargetDao {
    
    /**
     * 插入目标
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTarget(target: TargetEntity): Long
    
    /**
     * 更新目标
     */
    @Update
    suspend fun updateTarget(target: TargetEntity)
    
    /**
     * 删除目标
     */
    @Delete
    suspend fun deleteTarget(target: TargetEntity)
    
    /**
     * 插入目标进度
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTargetProgress(progress: TargetProgressEntity): Long
    
    /**
     * 更新目标进度
     */
    @Update
    suspend fun updateTargetProgress(progress: TargetProgressEntity)
    
    /**
     * 删除目标进度
     */
    @Delete
    suspend fun deleteTargetProgress(progress: TargetProgressEntity)
    
    /**
     * 根据ID获取目标
     */
    @Query("SELECT * FROM targets WHERE targetId = :targetId")
    suspend fun getTargetById(targetId: String): TargetEntity?
    
    /**
     * 根据ID获取目标及其进度
     */
    @Transaction
    @Query("SELECT * FROM targets WHERE targetId = :targetId")
    suspend fun getTargetWithProgressById(targetId: String): TargetWithProgress?
    
    /**
     * 获取用户的所有目标
     */
    @Query("SELECT * FROM targets WHERE userId = :userId ORDER BY createdAt DESC")
    fun getTargetsByUserId(userId: String): Flow<List<TargetEntity>>
    
    /**
     * 获取用户的所有目标及其进度
     */
    @Transaction
    @Query("SELECT * FROM targets WHERE userId = :userId ORDER BY createdAt DESC")
    fun getTargetsWithProgressByUserId(userId: String): Flow<List<TargetWithProgress>>
    
    /**
     * 获取用户的活跃目标
     */
    @Query("SELECT * FROM targets WHERE userId = :userId AND status = :status ORDER BY dueDate ASC")
    fun getTargetsByStatus(userId: String, status: TargetStatus): Flow<List<TargetEntity>>
    
    /**
     * 获取用户的活跃目标及其进度
     */
    @Transaction
    @Query("SELECT * FROM targets WHERE userId = :userId AND status = :status ORDER BY dueDate ASC")
    fun getTargetsWithProgressByStatus(userId: String, status: TargetStatus): Flow<List<TargetWithProgress>>
    
    /**
     * 获取用户的承诺
     */
    @Query("SELECT * FROM targets WHERE userId = :userId AND isPromise = 1 AND status = :status ORDER BY dueDate ASC")
    fun getPromisesByStatus(userId: String, status: TargetStatus): Flow<List<TargetEntity>>
    
    /**
     * 获取即将到期的目标
     */
    @Query("SELECT * FROM targets WHERE userId = :userId AND status = :status AND dueDate BETWEEN :startDate AND :endDate ORDER BY dueDate ASC")
    fun getUpcomingTargets(userId: String, status: TargetStatus, startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<TargetEntity>>
    
    /**
     * 更新目标状态
     */
    @Query("UPDATE targets SET status = :status, updatedAt = :updatedAt, syncStatus = :syncStatus WHERE targetId = :targetId")
    suspend fun updateTargetStatus(targetId: String, status: TargetStatus, updatedAt: LocalDateTime, syncStatus: SyncStatus = SyncStatus.PENDING)
    
    /**
     * 获取待同步的目标
     */
    @Query("SELECT * FROM targets WHERE syncStatus = :status")
    suspend fun getTargetsByStatus(status: SyncStatus): List<TargetEntity>
    
    /**
     * 更新目标同步状态
     */
    @Query("UPDATE targets SET syncStatus = :status WHERE targetId = :targetId")
    suspend fun updateTargetSyncStatus(targetId: String, status: SyncStatus)
    
    /**
     * 获取待同步的目标进度
     */
    @Query("SELECT * FROM target_progress WHERE syncStatus = :status")
    suspend fun getTargetProgressByStatus(status: SyncStatus): List<TargetProgressEntity>
    
    /**
     * 更新目标进度同步状态
     */
    @Query("UPDATE target_progress SET syncStatus = :status WHERE progressId = :progressId")
    suspend fun updateTargetProgressSyncStatus(progressId: String, status: SyncStatus)
    
    /**
     * 根据目标ID获取所有进度
     */
    @Query("SELECT * FROM target_progress WHERE targetId = :targetId ORDER BY dueDate ASC")
    fun getProgressByTargetId(targetId: String): Flow<List<TargetProgressEntity>>
    
    /**
     * 更新目标进度完成状态
     */
    @Query("UPDATE target_progress SET isCompleted = :isCompleted, completedAt = :completedAt, updatedAt = :updatedAt, syncStatus = :syncStatus WHERE progressId = :progressId")
    suspend fun updateProgressCompletionStatus(progressId: String, isCompleted: Boolean, completedAt: LocalDateTime?, updatedAt: LocalDateTime, syncStatus: SyncStatus = SyncStatus.PENDING)
} 