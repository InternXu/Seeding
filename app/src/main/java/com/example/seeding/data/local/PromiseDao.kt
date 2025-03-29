package com.example.seeding.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seeding.data.model.Promise
import kotlinx.coroutines.flow.Flow

@Dao
interface PromiseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromise(promise: Promise)

    @Update
    suspend fun updatePromise(promise: Promise)

    @Delete
    suspend fun deletePromise(promise: Promise)

    @Query("SELECT * FROM promises WHERE promiseId = :promiseId")
    fun getPromiseById(promiseId: String): Flow<Promise?>

    @Query("SELECT * FROM promises WHERE userId = :userId AND isCompleted = :isCompleted ORDER BY deadline ASC")
    fun getPromisesByUserAndStatus(userId: String, isCompleted: Boolean): Flow<List<Promise>>

    @Query("SELECT * FROM promises WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllUserPromises(userId: String): Flow<List<Promise>>

    @Query("UPDATE promises SET isCompleted = 1, completedAt = :completedAt WHERE promiseId = :promiseId")
    suspend fun completePromise(promiseId: String, completedAt: Long)

    @Query("SELECT * FROM promises WHERE userId = :userId AND deadline < :currentTime AND isCompleted = 0")
    suspend fun getOverduePromises(userId: String, currentTime: Long): List<Promise>
    
    @Query("SELECT * FROM promises WHERE relatedNegativeActionId = :actionId")
    fun getPromiseByNegativeAction(actionId: String): Flow<Promise?>
} 