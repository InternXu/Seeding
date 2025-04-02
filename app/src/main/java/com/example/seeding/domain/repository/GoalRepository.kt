package com.example.seeding.domain.repository

import com.example.seeding.data.model.Goal
import kotlinx.coroutines.flow.Flow

/**
 * 目标仓库接口 - 领域层
 * 定义目标相关的数据操作，不包含具体实现
 */
interface GoalRepository {
    /**
     * 获取用户的所有目标
     */
    fun getAllUserGoals(userId: String): Flow<List<Goal>>
    
    /**
     * 获取目标详情
     */
    suspend fun getGoalById(goalId: String): Goal?
    
    /**
     * 创建新目标
     */
    suspend fun createGoal(goal: Goal)
    
    /**
     * 更新目标
     */
    suspend fun updateGoal(goal: Goal)
    
    /**
     * 完成目标
     */
    suspend fun completeGoal(goalId: String)
    
    /**
     * 放弃目标（标记为删除）
     */
    suspend fun abandonGoal(goalId: String)
    
    /**
     * 彻底删除目标
     */
    suspend fun deleteGoal(goal: Goal)
    
    /**
     * 检查并处理过期目标
     */
    suspend fun checkForOverdueGoals(userId: String)
} 