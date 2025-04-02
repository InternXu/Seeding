package com.example.seeding.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seeding.data.local.entity.GoalEntity
import com.example.seeding.data.model.GoalStatus
import kotlinx.coroutines.flow.Flow

/**
 * 目标表数据访问对象
 */
@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity)
    
    @Update
    suspend fun updateGoal(goal: GoalEntity)
    
    @Delete
    suspend fun deleteGoal(goal: GoalEntity)
    
    @Query("SELECT * FROM goals WHERE goalId = :goalId")
    suspend fun getGoalById(goalId: String): GoalEntity?
    
    @Query("SELECT * FROM goals WHERE userId = :userId ORDER BY deadline ASC")
    fun getAllUserGoals(userId: String): Flow<List<GoalEntity>>
    
    @Query("SELECT * FROM goals WHERE userId = :userId AND status NOT IN (:excludeStatuses) ORDER BY deadline ASC")
    fun getActiveUserGoals(userId: String, excludeStatuses: List<GoalStatus>): Flow<List<GoalEntity>>
    
    @Query("SELECT * FROM goals WHERE userId = :userId AND status = :status ORDER BY deadline ASC")
    fun getUserGoalsByStatus(userId: String, status: GoalStatus): Flow<List<GoalEntity>>
    
    @Query("SELECT * FROM goals WHERE userId = :userId AND seedIds LIKE '%' || :seedId || '%' AND status NOT IN (:excludeStatuses) ORDER BY deadline ASC")
    fun getUserGoalsBySeed(userId: String, seedId: Int, excludeStatuses: List<GoalStatus>): Flow<List<GoalEntity>>
    
    @Query("UPDATE goals SET status = :status, lastUpdated = :timestamp WHERE goalId = :goalId")
    suspend fun updateGoalStatus(goalId: String, status: GoalStatus, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE goals SET isSynced = :synced WHERE goalId = :goalId")
    suspend fun updateSyncStatus(goalId: String, synced: Boolean)
    
    @Query("SELECT * FROM goals WHERE userId = :userId AND isSynced = 0")
    suspend fun getUnsyncedGoals(userId: String): List<GoalEntity>
    
    @Query("UPDATE goals SET status = :status, lastUpdated = :timestamp WHERE userId = :userId AND status = :currentStatus AND deadline < :currentTime")
    suspend fun updateOverdueGoals(userId: String, currentStatus: GoalStatus, status: GoalStatus, currentTime: Long, timestamp: Long = System.currentTimeMillis())
} 