package com.example.seeding.di

import com.example.seeding.data.repository.GoalRepositoryImpl
import com.example.seeding.data.repository.SeedRepositoryImpl
import com.example.seeding.data.repository.UserRepositoryImpl
import com.example.seeding.domain.repository.GoalRepository
import com.example.seeding.domain.repository.SeedRepository
import com.example.seeding.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 仓库依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindGoalRepository(impl: GoalRepositoryImpl): GoalRepository
    
    @Binds
    @Singleton
    abstract fun bindSeedRepository(impl: SeedRepositoryImpl): SeedRepository
} 