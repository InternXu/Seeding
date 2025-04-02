package com.example.seeding.di

import android.content.Context
import com.example.seeding.data.local.SeedingDatabase
import com.example.seeding.data.local.dao.ActionDao
import com.example.seeding.data.local.dao.CommitmentDao
import com.example.seeding.data.local.dao.GoalDao
import com.example.seeding.data.local.dao.SeedDao
import com.example.seeding.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSeedingDatabase(@ApplicationContext context: Context): SeedingDatabase {
        return SeedingDatabase.getDatabase(context)
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

    @Provides
    fun provideActionDao(database: SeedingDatabase): ActionDao {
        return database.actionDao()
    }

    @Provides
    fun provideCommitmentDao(database: SeedingDatabase): CommitmentDao {
        return database.commitmentDao()
    }
} 