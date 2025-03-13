package com.example.seeding.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.seeding.data.local.SeedingDatabase
import com.example.seeding.data.remote.SeedingApi
import com.example.seeding.data.repository.AuthRepositoryImpl
import com.example.seeding.data.repository.BehaviorRepositoryImpl
import com.example.seeding.data.repository.SeedRepositoryImpl
import com.example.seeding.data.repository.TargetRepositoryImpl
import com.example.seeding.domain.repository.AuthRepository
import com.example.seeding.domain.repository.BehaviorRepository
import com.example.seeding.domain.repository.SeedRepository
import com.example.seeding.domain.repository.TargetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Hilt依赖注入模块，提供应用程序级别的依赖
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * 提供Room数据库实例
     */
    @Provides
    @Singleton
    fun provideSeedingDatabase(
        @ApplicationContext context: Context
    ): SeedingDatabase {
        return Room.databaseBuilder(
            context,
            SeedingDatabase::class.java,
            "seeding_database"
        ).fallbackToDestructiveMigration().build()
    }

    /**
     * 提供OkHttpClient实例，用于网络请求
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * 提供Retrofit实例，用于API调用
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/") // 将来替换为实际API地址
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * 提供API服务接口
     */
    @Provides
    @Singleton
    fun provideSeedingApi(retrofit: Retrofit): SeedingApi {
        return retrofit.create(SeedingApi::class.java)
    }

    /**
     * 提供WorkManager实例，用于后台任务
     */
    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    /**
     * 提供认证仓库实现
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        api: SeedingApi,
        db: SeedingDatabase
    ): AuthRepository {
        return AuthRepositoryImpl(api, db)
    }

    /**
     * 提供行为仓库实现
     */
    @Provides
    @Singleton
    fun provideBehaviorRepository(
        api: SeedingApi,
        db: SeedingDatabase
    ): BehaviorRepository {
        return BehaviorRepositoryImpl(api, db)
    }

    /**
     * 提供目标仓库实现
     */
    @Provides
    @Singleton
    fun provideTargetRepository(
        api: SeedingApi,
        db: SeedingDatabase
    ): TargetRepository {
        return TargetRepositoryImpl(api, db)
    }

    /**
     * 提供种子仓库实现
     */
    @Provides
    @Singleton
    fun provideSeedRepository(
        api: SeedingApi,
        db: SeedingDatabase
    ): SeedRepository {
        return SeedRepositoryImpl(api, db)
    }
} 