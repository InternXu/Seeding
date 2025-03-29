package com.example.seeding.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seeding.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * 用户DAO - 数据层（数据库）
 * 定义用户表的数据库操作
 */
@Dao
interface UserDao {
    /**
     * 插入用户，如果已存在则替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    /**
     * 根据用户ID获取用户
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?
    
    /**
     * 根据手机号获取用户
     */
    @Query("SELECT * FROM users WHERE phoneNumber = :phoneNumber")
    suspend fun getUserByPhoneNumber(phoneNumber: String): UserEntity?
    
    /**
     * 获取当前登录用户
     */
    @Query("SELECT * FROM users WHERE isCurrentUser = 1 LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>
    
    /**
     * 标记用户为当前登录用户
     */
    @Query("UPDATE users SET isCurrentUser = :isCurrentUser WHERE id = :userId")
    suspend fun setCurrentUser(userId: String, isCurrentUser: Boolean)
    
    /**
     * 更新用户信息
     */
    @Update
    suspend fun updateUser(user: UserEntity)
    
    /**
     * 清除当前登录用户标记
     */
    @Query("UPDATE users SET isCurrentUser = 0")
    suspend fun clearCurrentUser()
    
    /**
     * 删除所有用户
     */
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
} 