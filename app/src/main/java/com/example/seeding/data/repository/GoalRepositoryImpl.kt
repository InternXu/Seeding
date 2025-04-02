package com.example.seeding.data.repository

import com.example.seeding.data.local.dao.GoalDao
import com.example.seeding.data.model.Goal
import com.example.seeding.data.model.GoalStatus
import com.example.seeding.data.util.EntityMappers
import com.example.seeding.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {
    
    override fun getAllUserGoals(userId: String): Flow<List<Goal>> {
        return goalDao.getAllUserGoals(userId).map { entities ->
            EntityMappers.mapToGoalList(entities)
        }
    }
    
    override suspend fun getGoalById(goalId: String): Goal? {
        val goalEntity = goalDao.getGoalById(goalId) ?: return null
        return EntityMappers.mapToGoal(goalEntity)
    }
    
    override suspend fun createGoal(goal: Goal) {
        val goalEntity = EntityMappers.mapToGoalEntity(goal)
        goalDao.insertGoal(goalEntity)
    }
    
    override suspend fun updateGoal(goal: Goal) {
        val goalEntity = EntityMappers.mapToGoalEntity(goal)
        goalDao.updateGoal(goalEntity)
    }
    
    override suspend fun completeGoal(goalId: String) {
        val currentTime = System.currentTimeMillis()
        val goal = getGoalById(goalId) ?: return
        
        val newStatus = if (goal.deadline < currentTime) {
            GoalStatus.OVERDUE_COMPLETED
        } else {
            GoalStatus.COMPLETED
        }
        
        goalDao.updateGoalStatus(goalId, newStatus)
    }
    
    override suspend fun abandonGoal(goalId: String) {
        goalDao.updateGoalStatus(goalId, GoalStatus.ABANDONED)
    }
    
    override suspend fun deleteGoal(goal: Goal) {
        val goalEntity = EntityMappers.mapToGoalEntity(goal)
        goalDao.deleteGoal(goalEntity)
    }
    
    override suspend fun checkForOverdueGoals(userId: String) {
        val currentTime = System.currentTimeMillis()
        goalDao.updateOverdueGoals(
            userId = userId,
            currentStatus = GoalStatus.IN_PROGRESS,
            status = GoalStatus.OVERDUE,
            currentTime = currentTime
        )
    }
}
