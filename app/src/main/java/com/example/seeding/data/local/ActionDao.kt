package com.example.seeding.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seeding.data.model.Action
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: Action)

    @Update
    suspend fun updateAction(action: Action)

    @Delete
    suspend fun deleteAction(action: Action)

    @Query("SELECT * FROM actions WHERE actionId = :actionId")
    fun getActionById(actionId: String): Flow<Action?>

    @Query("SELECT * FROM actions WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllUserActions(userId: String): Flow<List<Action>>

    @Query("SELECT * FROM actions WHERE userId = :userId AND isPositive = :isPositive ORDER BY createdAt DESC")
    fun getActionsByUserAndType(userId: String, isPositive: Boolean): Flow<List<Action>>

    @Query("SELECT * FROM actions WHERE userId = :userId AND :seedId IN (seedIds) ORDER BY createdAt DESC")
    fun getActionsBySeed(userId: String, seedId: Int): Flow<List<Action>>
    
    @Query("SELECT COUNT(*) FROM actions WHERE userId = :userId AND isPositive = 1")
    fun getPositiveActionCount(userId: String): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM actions WHERE userId = :userId AND isPositive = 0")
    fun getNegativeActionCount(userId: String): Flow<Int>
} 