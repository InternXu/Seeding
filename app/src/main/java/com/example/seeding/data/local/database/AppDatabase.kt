package com.example.seeding.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.seeding.data.local.dao.UserDao
import com.example.seeding.data.local.entity.UserEntity

/**
 * 应用数据库 - 数据层（数据库）
 * 定义数据库及其版本，包含所有实体和DAO
 */
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * 用户DAO
     */
    abstract fun userDao(): UserDao
    
    companion object {
        private const val DATABASE_NAME = "seeding_database"
        
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration() // 数据库版本升级时，重新创建数据库
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 