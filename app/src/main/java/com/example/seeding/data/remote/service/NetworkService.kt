package com.example.seeding.data.remote.service

import com.example.seeding.data.remote.api.UserApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 网络服务管理类 - 数据层（远程服务）
 * 负责创建和管理Retrofit实例和API服务
 */
@Singleton
class NetworkService @Inject constructor() {
    
    companion object {
        private const val BASE_URL = "https://api.seeding.com/" // 服务器地址，可根据环境配置
        private const val TIMEOUT = 30L // 超时时间(秒)
    }
    
    /**
     * 创建OkHttpClient
     */
    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // 添加日志拦截器
            .addInterceptor { chain ->
                // 添加公共Header，如Authorization
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * 创建Retrofit实例
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * 用户API服务
     */
    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
} 