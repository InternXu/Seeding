package com.example.seeding.domain.repository

import com.example.seeding.data.model.Commitment
import com.example.seeding.data.model.CommitmentStatus
import kotlinx.coroutines.flow.Flow

interface CommitmentRepository {
    /**
     * 获取用户所有承诺
     */
    fun getAllCommitmentsByUserId(userId: String): Flow<List<Commitment>>
    
    /**
     * 获取用户当前活跃的承诺（未完成且未过期的）
     */
    fun getActiveCommitments(userId: String): Flow<List<Commitment>>
    
    /**
     * 获取用户所有活跃承诺，包括未完成未过期的，以及逾期未超过12小时的
     */
    fun getAllActiveCommitments(userId: String): Flow<List<Commitment>>
    
    /**
     * 根据ID获取承诺
     */
    suspend fun getCommitmentById(commitmentId: String): Commitment?
    
    /**
     * 根据行为ID获取承诺
     */
    suspend fun getCommitmentByActionId(actionId: String): Commitment?
    
    /**
     * 添加新承诺
     */
    suspend fun addCommitment(commitment: Commitment): String
    
    /**
     * 更新承诺状态
     */
    suspend fun updateCommitmentStatus(commitmentId: String, status: CommitmentStatus)
    
    /**
     * 删除承诺
     */
    suspend fun deleteCommitment(commitment: Commitment)
    
    /**
     * 检查并更新过期的承诺
     */
    suspend fun checkAndUpdateExpiredCommitments(userId: String)
} 