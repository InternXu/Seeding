package com.example.seeding.domain.repository

import com.example.seeding.data.model.Action
import kotlinx.coroutines.flow.Flow

interface ActionRepository {
    /**
     * 获取用户所有行为
     */
    fun getActionsByUserId(userId: String): Flow<List<Action>>
    
    /**
     * 根据ID获取行为
     */
    suspend fun getActionById(actionId: String): Action?
    
    /**
     * 添加新行为
     */
    suspend fun addAction(action: Action): String
    
    /**
     * 删除行为
     */
    suspend fun deleteAction(action: Action)
    
    /**
     * 更新行为的承诺状态
     */
    suspend fun updateActionHasCommitment(actionId: String, hasCommitment: Boolean)
    
    /**
     * 根据关键字搜索行为
     */
    fun searchActionsByKeyword(userId: String, keyword: String): Flow<List<Action>>
} 