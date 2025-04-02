package com.example.seeding.data.repository

import com.example.seeding.data.local.dao.CommitmentDao
import com.example.seeding.data.model.Commitment
import com.example.seeding.data.model.CommitmentStatus
import com.example.seeding.domain.repository.CommitmentRepository
import com.example.seeding.util.EntityMappers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommitmentRepositoryImpl @Inject constructor(
    private val commitmentDao: CommitmentDao
) : CommitmentRepository {

    override fun getAllCommitmentsByUserId(userId: String): Flow<List<Commitment>> {
        return commitmentDao.getAllCommitmentsByUserId(userId).map { entities ->
            entities.map { EntityMappers.mapToCommitment(it) }
        }
    }

    override fun getActiveCommitments(userId: String): Flow<List<Commitment>> {
        return commitmentDao.getActiveCommitments(userId).map { entities ->
            entities.map { EntityMappers.mapToCommitment(it) }
        }
    }

    override fun getAllActiveCommitments(userId: String): Flow<List<Commitment>> {
        return commitmentDao.getAllActiveCommitments(userId).map { entities ->
            entities.map { EntityMappers.mapToCommitment(it) }
        }
    }

    override suspend fun getCommitmentById(commitmentId: String): Commitment? {
        return commitmentDao.getCommitmentById(commitmentId)?.let {
            EntityMappers.mapToCommitment(it)
        }
    }

    override suspend fun getCommitmentByActionId(actionId: String): Commitment? {
        return commitmentDao.getCommitmentByActionId(actionId)?.let {
            EntityMappers.mapToCommitment(it)
        }
    }

    override suspend fun addCommitment(commitment: Commitment): String {
        val entity = EntityMappers.mapToCommitmentEntity(commitment)
        commitmentDao.insertCommitment(entity)
        return commitment.commitmentId
    }

    override suspend fun updateCommitmentStatus(commitmentId: String, status: CommitmentStatus) {
        commitmentDao.updateStatus(commitmentId, status)
    }

    override suspend fun deleteCommitment(commitment: Commitment) {
        val entity = EntityMappers.mapToCommitmentEntity(commitment)
        commitmentDao.deleteCommitment(entity)
    }

    override suspend fun checkAndUpdateExpiredCommitments(userId: String) {
        commitmentDao.updateExpiredCommitments(userId)
    }
} 