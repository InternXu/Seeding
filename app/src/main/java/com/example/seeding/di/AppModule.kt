package com.example.seeding.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * 应用程序全局依赖注入模块
 * 提供应用级别的依赖项
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    // 所有仓库绑定已移至RepositoryModule
}
