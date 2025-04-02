package com.example.seeding.data.repository

import com.example.seeding.data.model.Action
import kotlinx.coroutines.flow.Flow

/**
 * 行为仓库接口，定义与行为相关的数据操作
 */
interface ActionRepository {
    
    /**
     * 获取所有行为记录
     * @return 行为记录流
     */
    fun getAllActions(): Flow<List<Action>>
    
    /**
     * 获取特定类型的行为记录
     * @param type 行为类型
     * @return 特定类型的行为记录流
     */
    fun getActionsByType(type: String): Flow<List<Action>>
    
    /**
     * 根据ID获取特定行为
     * @param actionId 行为ID
     * @return 行为对象
     */
    suspend fun getActionById(actionId: String): Action?
    
    /**
     * 插入新行为记录
     * @param action 行为对象
     */
    suspend fun insertAction(action: Action)
    
    /**
     * 更新行为记录
     * @param action 行为对象
     */
    suspend fun updateAction(action: Action)
    
    /**
     * 删除行为记录
     * @param actionId 行为ID
     */
    suspend fun deleteAction(actionId: String)
} 