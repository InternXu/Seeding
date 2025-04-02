package com.example.seeding.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seeding.data.local.entity.CommitmentEntity
import com.example.seeding.data.model.CommitmentStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface CommitmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommitment(commitment: CommitmentEntity): Long

    @Update
    suspend fun updateCommitment(commitment: CommitmentEntity)

    @Delete
    suspend fun deleteCommitment(commitment: CommitmentEntity)

    @Query("SELECT * FROM commitments WHERE commitmentId = :commitmentId")
    suspend fun getCommitmentById(commitmentId: String): CommitmentEntity?

    @Query("SELECT * FROM commitments WHERE actionId = :actionId")
    suspend fun getCommitmentByActionId(actionId: String): CommitmentEntity?

    @Query("SELECT * FROM commitments WHERE userId = :userId AND status = :status ORDER BY deadline ASC")
    fun getCommitmentsByUserIdAndStatus(userId: String, status: CommitmentStatus): Flow<List<CommitmentEntity>>

    @Query("SELECT * FROM commitments WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllCommitmentsByUserId(userId: String): Flow<List<CommitmentEntity>>

    @Query("SELECT * FROM commitments WHERE userId = :userId AND status = :status AND deadline > :currentTime ORDER BY deadline ASC")
    fun getActiveCommitments(userId: String, status: CommitmentStatus = CommitmentStatus.PENDING, currentTime: Long = System.currentTimeMillis()): Flow<List<CommitmentEntity>>

    @Query("SELECT * FROM commitments WHERE userId = :userId AND ((status = :pendingStatus AND deadline > :currentTime) OR ((status = :unfulfilledStatus OR status = :expiredStatus) AND (:currentTime - deadline) < :graceTimeMillis)) ORDER BY deadline ASC")
    fun getAllActiveCommitments(
        userId: String,
        pendingStatus: CommitmentStatus = CommitmentStatus.PENDING,
        unfulfilledStatus: CommitmentStatus = CommitmentStatus.UNFULFILLED,
        expiredStatus: CommitmentStatus = CommitmentStatus.EXPIRED,
        currentTime: Long = System.currentTimeMillis(),
        graceTimeMillis: Long = 12 * 60 * 60 * 1000 // 12小时
    ): Flow<List<CommitmentEntity>>

    @Query("UPDATE commitments SET status = :status WHERE commitmentId = :commitmentId")
    suspend fun updateStatus(commitmentId: String, status: CommitmentStatus)

    @Query("UPDATE commitments SET status = :newStatus WHERE userId = :userId AND status = :status AND deadline < :currentTime")
    suspend fun updateExpiredCommitments(userId: String, status: CommitmentStatus = CommitmentStatus.PENDING, newStatus: CommitmentStatus = CommitmentStatus.UNFULFILLED, currentTime: Long = System.currentTimeMillis())
} 