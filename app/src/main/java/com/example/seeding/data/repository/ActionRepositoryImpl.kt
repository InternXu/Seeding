package com.example.seeding.data.repository

import com.example.seeding.data.local.dao.ActionDao
import com.example.seeding.data.local.entity.ActionEntity
import com.example.seeding.data.model.Action
import com.example.seeding.domain.repository.ActionRepository
import com.example.seeding.util.EntityMappers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionRepositoryImpl @Inject constructor(
    private val actionDao: ActionDao
) : ActionRepository {

    override fun getActionsByUserId(userId: String): Flow<List<Action>> {
        return actionDao.getActionsByUserId(userId).map { entities ->
            entities.map { EntityMappers.mapToAction(it) }
        }
    }

    override suspend fun getActionById(actionId: String): Action? {
        return actionDao.getActionById(actionId)?.let { 
            EntityMappers.mapToAction(it) 
        }
    }

    override suspend fun addAction(action: Action): String {
        val entity = EntityMappers.mapToActionEntity(action)
        actionDao.insertAction(entity)
        return action.actionId
    }

    override suspend fun deleteAction(action: Action) {
        val entity = EntityMappers.mapToActionEntity(action)
        actionDao.deleteAction(entity)
    }

    override suspend fun updateActionHasCommitment(actionId: String, hasCommitment: Boolean) {
        actionDao.updateHasCommitment(actionId, hasCommitment)
    }

    override fun searchActionsByKeyword(userId: String, keyword: String): Flow<List<Action>> {
        return actionDao.searchActionsByKeyword(userId, keyword).map { entities ->
            entities.map { EntityMappers.mapToAction(it) }
        }
    }
} 