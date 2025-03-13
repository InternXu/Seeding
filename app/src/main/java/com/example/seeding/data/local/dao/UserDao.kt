package com.example.seeding.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seeding.data.local.entity.SyncStatus
import com.example.seeding.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * 用户数据访问对象接口
 */
@Dao
interface UserDao {
    
    /**
     * 插入用户
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    /**
     * 更新用户
     */
    @Update
    suspend fun updateUser(user: UserEntity)
    
    /**
     * 删除用户
     */
    @Delete
    suspend fun deleteUser(user: UserEntity)
    
    /**
     * 根据ID获取用户
     */
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: String): UserEntity?
    
    /**
     * 根据邮箱获取用户
     */
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?
    
    /**
     * 获取当前登录用户
     * 假设只有一个用户处于登录状态
     */
    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>
    
    /**
     * 获取待同步的用户
     */
    @Query("SELECT * FROM users WHERE syncStatus = :status")
    suspend fun getUsersByStatus(status: SyncStatus): List<UserEntity>
    
    /**
     * 更新用户同步状态
     */
    @Query("UPDATE users SET syncStatus = :status WHERE userId = :userId")
    suspend fun updateUserSyncStatus(userId: String, status: SyncStatus)
    
    /**
     * 更新用户种子币数量
     */
    @Query("UPDATE users SET seedCoins = :coins WHERE userId = :userId")
    suspend fun updateUserSeedCoins(userId: String, coins: Int)
} 