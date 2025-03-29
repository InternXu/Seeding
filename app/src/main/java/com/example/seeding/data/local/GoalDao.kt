package com.example.seeding.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seeding.data.model.Goal
import com.example.seeding.data.model.GoalStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Query("SELECT * FROM goals WHERE goalId = :goalId")
    fun getGoalById(goalId: String): Flow<Goal?>

    @Query("SELECT * FROM goals WHERE userId = :userId AND status = :status ORDER BY deadline ASC")
    fun getGoalsByUserAndStatus(userId: String, status: GoalStatus): Flow<List<Goal>>

    @Query("SELECT * FROM goals WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllUserGoals(userId: String): Flow<List<Goal>>

    @Query("SELECT * FROM goals WHERE userId = :userId AND status IN (:statuses) ORDER BY deadline ASC")
    fun getGoalsByUserAndStatuses(userId: String, statuses: List<GoalStatus>): Flow<List<Goal>>

    @Query("UPDATE goals SET status = :status WHERE goalId = :goalId")
    suspend fun updateGoalStatus(goalId: String, status: GoalStatus)

    @Query("SELECT * FROM goals WHERE userId = :userId AND deadline < :currentTime AND status = :status")
    suspend fun getOverdueGoals(userId: String, currentTime: Long, status: GoalStatus = GoalStatus.IN_PROGRESS): List<Goal>
} 