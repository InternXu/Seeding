package com.example.seeding.data.remote

import com.example.seeding.data.remote.dto.AuthRequest
import com.example.seeding.data.remote.dto.AuthResponse
import com.example.seeding.data.remote.dto.BehaviorDto
import com.example.seeding.data.remote.dto.BehaviorRequest
import com.example.seeding.data.remote.dto.BehaviorResponse
import com.example.seeding.data.remote.dto.RegisterRequest
import com.example.seeding.data.remote.dto.SeedDto
import com.example.seeding.data.remote.dto.TargetDto
import com.example.seeding.data.remote.dto.TargetProgressDto
import com.example.seeding.data.remote.dto.TargetRequest
import com.example.seeding.data.remote.dto.TargetResponse
import com.example.seeding.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Seeding API接口
 * 定义与后端服务器的通信接口
 */
interface SeedingApi {
    
    // 认证相关接口
    
    /**
     * 用户登录
     */
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
    
    /**
     * 用户注册
     */
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    /**
     * 刷新令牌
     */
    @POST("auth/refresh")
    suspend fun refreshToken(@Header("Authorization") refreshToken: String): Response<AuthResponse>
    
    /**
     * 获取当前用户信息
     */
    @GET("users/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<UserDto>
    
    // 种子相关接口
    
    /**
     * 获取所有种子
     */
    @GET("seeds")
    suspend fun getAllSeeds(@Header("Authorization") token: String): Response<List<SeedDto>>
    
    /**
     * 根据ID获取种子
     */
    @GET("seeds/{seedId}")
    suspend fun getSeedById(
        @Header("Authorization") token: String,
        @Path("seedId") seedId: Int
    ): Response<SeedDto>
    
    /**
     * 根据类别获取种子
     */
    @GET("seeds/category/{category}")
    suspend fun getSeedsByCategory(
        @Header("Authorization") token: String,
        @Path("category") category: String
    ): Response<List<SeedDto>>
    
    // 目标相关接口
    
    /**
     * 创建目标
     */
    @POST("targets")
    suspend fun createTarget(
        @Header("Authorization") token: String,
        @Body request: TargetRequest
    ): Response<TargetResponse>
    
    /**
     * 更新目标
     */
    @PUT("targets/{targetId}")
    suspend fun updateTarget(
        @Header("Authorization") token: String,
        @Path("targetId") targetId: String,
        @Body request: TargetRequest
    ): Response<TargetResponse>
    
    /**
     * 获取用户的所有目标
     */
    @GET("targets")
    suspend fun getUserTargets(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null,
        @Query("isPromise") isPromise: Boolean? = null
    ): Response<List<TargetDto>>
    
    /**
     * 根据ID获取目标
     */
    @GET("targets/{targetId}")
    suspend fun getTargetById(
        @Header("Authorization") token: String,
        @Path("targetId") targetId: String
    ): Response<TargetDto>
    
    /**
     * 更新目标状态
     */
    @PUT("targets/{targetId}/status")
    suspend fun updateTargetStatus(
        @Header("Authorization") token: String,
        @Path("targetId") targetId: String,
        @Query("status") status: String
    ): Response<TargetDto>
    
    /**
     * 添加目标进度
     */
    @POST("targets/{targetId}/progress")
    suspend fun addTargetProgress(
        @Header("Authorization") token: String,
        @Path("targetId") targetId: String,
        @Body progress: TargetProgressDto
    ): Response<TargetProgressDto>
    
    /**
     * 更新目标进度
     */
    @PUT("targets/{targetId}/progress/{progressId}")
    suspend fun updateTargetProgress(
        @Header("Authorization") token: String,
        @Path("targetId") targetId: String,
        @Path("progressId") progressId: String,
        @Body progress: TargetProgressDto
    ): Response<TargetProgressDto>
    
    /**
     * 删除目标
     */
    @DELETE("targets/{targetId}")
    suspend fun deleteTarget(
        @Header("Authorization") token: String,
        @Path("targetId") targetId: String
    ): Response<Unit>
    
    // 行为相关接口
    
    /**
     * 创建行为
     */
    @POST("behaviors")
    suspend fun createBehavior(
        @Header("Authorization") token: String,
        @Body request: BehaviorRequest
    ): Response<BehaviorResponse>
    
    /**
     * 获取用户的所有行为
     */
    @GET("behaviors")
    suspend fun getUserBehaviors(
        @Header("Authorization") token: String,
        @Query("isPositive") isPositive: Boolean? = null,
        @Query("seedId") seedId: Int? = null,
        @Query("targetId") targetId: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<List<BehaviorDto>>
    
    /**
     * 根据ID获取行为
     */
    @GET("behaviors/{behaviorId}")
    suspend fun getBehaviorById(
        @Header("Authorization") token: String,
        @Path("behaviorId") behaviorId: String
    ): Response<BehaviorDto>
    
    /**
     * 更新行为
     */
    @PUT("behaviors/{behaviorId}")
    suspend fun updateBehavior(
        @Header("Authorization") token: String,
        @Path("behaviorId") behaviorId: String,
        @Body request: BehaviorRequest
    ): Response<BehaviorResponse>
    
    /**
     * 删除行为
     */
    @DELETE("behaviors/{behaviorId}")
    suspend fun deleteBehavior(
        @Header("Authorization") token: String,
        @Path("behaviorId") behaviorId: String
    ): Response<Unit>
} 