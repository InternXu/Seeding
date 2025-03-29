package com.example.seeding.data.di

import android.content.Context
import com.example.seeding.data.local.ActionDao
import com.example.seeding.data.local.GoalDao
import com.example.seeding.data.local.PromiseDao
import com.example.seeding.data.local.SeedingDatabase
import com.example.seeding.data.local.UserDao
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
    @Singleton
    fun provideUserDao(database: SeedingDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideGoalDao(database: SeedingDatabase): GoalDao {
        return database.goalDao()
    }

    @Provides
    @Singleton
    fun providePromiseDao(database: SeedingDatabase): PromiseDao {
        return database.promiseDao()
    }

    @Provides
    @Singleton
    fun provideActionDao(database: SeedingDatabase): ActionDao {
        return database.actionDao()
    }
} 