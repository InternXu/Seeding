package com.example.seeding.data.repository

import com.example.seeding.data.local.PromiseDao
import com.example.seeding.data.model.Promise
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromiseRepository @Inject constructor(
    private val promiseDao: PromiseDao
) {
    fun getPromiseById(promiseId: String): Flow<Promise?> = promiseDao.getPromiseById(promiseId)
    
    fun getActivePromises(userId: String): Flow<List<Promise>> = 
        promiseDao.getPromisesByUserAndStatus(userId, false)
    
    fun getCompletedPromises(userId: String): Flow<List<Promise>> = 
        promiseDao.getPromisesByUserAndStatus(userId, true)
    
    fun getAllUserPromises(userId: String): Flow<List<Promise>> = 
        promiseDao.getAllUserPromises(userId)
    
    fun getPromiseByNegativeAction(actionId: String): Flow<Promise?> = 
        promiseDao.getPromiseByNegativeAction(actionId)
    
    suspend fun createPromise(promise: Promise) = promiseDao.insertPromise(promise)
    
    suspend fun updatePromise(promise: Promise) = promiseDao.updatePromise(promise)
    
    suspend fun deletePromise(promise: Promise) = promiseDao.deletePromise(promise)
    
    suspend fun completePromise(promiseId: String) {
        val currentTime = System.currentTimeMillis()
        promiseDao.completePromise(promiseId, currentTime)
    }
    
    suspend fun checkForOverduePromises(userId: String): List<Promise> {
        val currentTime = System.currentTimeMillis()
        return promiseDao.getOverduePromises(userId, currentTime)
    }
} 