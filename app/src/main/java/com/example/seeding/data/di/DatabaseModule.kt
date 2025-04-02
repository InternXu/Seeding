package com.example.seeding.data.di

import android.content.Context
import com.example.seeding.data.local.dao.GoalDao
import com.example.seeding.data.local.dao.SeedDao
import com.example.seeding.data.local.dao.UserDao
import com.example.seeding.data.local.database.SeedingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 数据库依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideSeedingDatabase(@ApplicationContext context: Context): SeedingDatabase {
        return SeedingDatabase.getInstance(context)
    }
    
    @Provides
    fun provideUserDao(database: SeedingDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    fun provideSeedDao(database: SeedingDatabase): SeedDao {
        return database.seedDao()
    }
    
    @Provides
    fun provideGoalDao(database: SeedingDatabase): GoalDao {
        return database.goalDao()
    }
} 