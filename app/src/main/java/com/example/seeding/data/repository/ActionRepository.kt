package com.example.seeding.data.repository

import com.example.seeding.data.local.ActionDao
import com.example.seeding.data.model.Action
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionRepository @Inject constructor(
    private val actionDao: ActionDao
) {
    fun getActionById(actionId: String): Flow<Action?> = actionDao.getActionById(actionId)
    
    fun getAllUserActions(userId: String): Flow<List<Action>> = 
        actionDao.getAllUserActions(userId)
    
    fun getPositiveActions(userId: String): Flow<List<Action>> = 
        actionDao.getActionsByUserAndType(userId, true)
    
    fun getNegativeActions(userId: String): Flow<List<Action>> = 
        actionDao.getActionsByUserAndType(userId, false)
    
    fun getActionsBySeed(userId: String, seedId: Int): Flow<List<Action>> = 
        actionDao.getActionsBySeed(userId, seedId)
    
    fun getPositiveActionCount(userId: String): Flow<Int> = 
        actionDao.getPositiveActionCount(userId)
    
    fun getNegativeActionCount(userId: String): Flow<Int> = 
        actionDao.getNegativeActionCount(userId)
    
    suspend fun createAction(action: Action) = actionDao.insertAction(action)
    
    suspend fun updateAction(action: Action) = actionDao.updateAction(action)
    
    suspend fun deleteAction(action: Action) = actionDao.deleteAction(action)
} 