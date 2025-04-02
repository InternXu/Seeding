package com.example.seeding.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seeding.data.local.entity.ActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: ActionEntity): Long

    @Update
    suspend fun updateAction(action: ActionEntity)

    @Delete
    suspend fun deleteAction(action: ActionEntity)

    @Query("SELECT * FROM actions WHERE actionId = :actionId")
    suspend fun getActionById(actionId: String): ActionEntity?

    @Query("SELECT * FROM actions WHERE userId = :userId ORDER BY createdAt DESC")
    fun getActionsByUserId(userId: String): Flow<List<ActionEntity>>

    @Query("UPDATE actions SET hasCommitment = :hasCommitment WHERE actionId = :actionId")
    suspend fun updateHasCommitment(actionId: String, hasCommitment: Boolean)

    @Query("SELECT * FROM actions WHERE userId = :userId AND content LIKE '%' || :keyword || '%' ORDER BY createdAt DESC")
    fun searchActionsByKeyword(userId: String, keyword: String): Flow<List<ActionEntity>>
} 