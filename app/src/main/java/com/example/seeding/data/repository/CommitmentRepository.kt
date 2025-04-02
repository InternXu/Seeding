package com.example.seeding.data.repository

import com.example.seeding.data.model.Commitment
import com.example.seeding.data.model.CommitmentStatus
import kotlinx.coroutines.flow.Flow

/**
 * 承诺仓库接口，定义与承诺相关的数据操作
 */
interface CommitmentRepository {
    
    /**
     * 获取所有承诺记录
     * @return 承诺记录流
     */
    fun getAllCommitments(): Flow<List<Commitment>>
    
    /**
     * 获取未完成的承诺记录
     * @return 未完成的承诺记录流
     */
    fun getPendingCommitments(): Flow<List<Commitment>>
    
    /**
     * 获取特定状态的承诺
     * @param status 承诺状态
     * @return 特定状态的承诺记录流
     */
    fun getCommitmentsByStatus(status: CommitmentStatus): Flow<List<Commitment>>
    
    /**
     * 根据ID获取特定承诺
     * @param commitmentId 承诺ID
     * @return 承诺对象
     */
    suspend fun getCommitmentById(commitmentId: String): Commitment?
    
    /**
     * 获取特定行为的承诺
     * @param actionId 行为ID
     * @return 承诺对象
     */
    suspend fun getCommitmentByActionId(actionId: String): Commitment?
    
    /**
     * 插入新承诺记录
     * @param commitment 承诺对象
     */
    suspend fun insertCommitment(commitment: Commitment)
    
    /**
     * 更新承诺记录
     * @param commitment 承诺对象
     */
    suspend fun updateCommitment(commitment: Commitment)
    
    /**
     * 更新承诺状态
     * @param commitmentId 承诺ID
     * @param status 新的状态
     */
    suspend fun updateCommitmentStatus(commitmentId: String, status: CommitmentStatus)
    
    /**
     * 完成承诺
     * @param commitmentId 承诺ID
     */
    suspend fun fulfillCommitment(commitmentId: String)
    
    /**
     * 检查并更新所有过期承诺
     */
    suspend fun checkAndUpdateExpiredCommitments()
    
    /**
     * 删除承诺记录
     * @param commitmentId 承诺ID
     */
    suspend fun deleteCommitment(commitmentId: String)
} 