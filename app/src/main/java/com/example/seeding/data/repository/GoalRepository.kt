package com.example.seeding.data.repository

import com.example.seeding.data.local.GoalDao
import com.example.seeding.data.model.Goal
import com.example.seeding.data.model.GoalStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepository @Inject constructor(
    private val goalDao: GoalDao
) {
    fun getGoalById(goalId: String): Flow<Goal?> = goalDao.getGoalById(goalId)
    
    fun getActiveGoals(userId: String): Flow<List<Goal>> = 
        goalDao.getGoalsByUserAndStatus(userId, GoalStatus.IN_PROGRESS)
    
    fun getCompletedGoals(userId: String): Flow<List<Goal>> = 
        goalDao.getGoalsByUserAndStatus(userId, GoalStatus.COMPLETED)
    
    fun getAbandonedGoals(userId: String): Flow<List<Goal>> = 
        goalDao.getGoalsByUserAndStatus(userId, GoalStatus.ABANDONED)
    
    fun getOverdueGoals(userId: String): Flow<List<Goal>> = 
        goalDao.getGoalsByUserAndStatuses(userId, listOf(GoalStatus.OVERDUE, GoalStatus.OVERDUE_COMPLETED))
    
    fun getAllUserGoals(userId: String): Flow<List<Goal>> = goalDao.getAllUserGoals(userId)
    
    suspend fun createGoal(goal: Goal) = goalDao.insertGoal(goal)
    
    suspend fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)
    
    suspend fun deleteGoal(goal: Goal) = goalDao.deleteGoal(goal)
    
    suspend fun completeGoal(goalId: String) = 
        goalDao.updateGoalStatus(goalId, GoalStatus.COMPLETED)
    
    suspend fun abandonGoal(goalId: String) = 
        goalDao.updateGoalStatus(goalId, GoalStatus.ABANDONED)
    
    suspend fun checkForOverdueGoals(userId: String) {
        val currentTime = System.currentTimeMillis()
        val overdueGoals = goalDao.getOverdueGoals(userId, currentTime)
        
        for (goal in overdueGoals) {
            goalDao.updateGoalStatus(goal.goalId, GoalStatus.OVERDUE)
        }
    }
} 