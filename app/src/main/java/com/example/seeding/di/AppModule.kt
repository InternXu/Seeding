package com.example.seeding.di

import android.content.Context
import com.example.seeding.data.local.dao.UserDao
import com.example.seeding.data.local.database.AppDatabase
import com.example.seeding.data.remote.service.NetworkService
import com.example.seeding.data.repository.UserRepositoryImpl
import com.example.seeding.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 应用依赖注入模块
 * 提供应用所需的依赖实例
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    /**
     * 提供数据库实例
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
    
    /**
     * 提供用户DAO
     */
    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
    
    /**
     * 提供网络服务
     */
    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService {
        return NetworkService()
    }
    
    /**
     * 提供用户仓库实现
     */
    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
        networkService: NetworkService
    ): UserRepository {
        return UserRepositoryImpl(userDao, networkService)
    }
} 