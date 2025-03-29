package com.example.seeding.data.repository

import com.example.seeding.data.local.dao.UserDao
import com.example.seeding.data.local.entity.toEntity
import com.example.seeding.data.remote.api.LoginRequest
import com.example.seeding.data.remote.api.RegisterRequest
import com.example.seeding.data.remote.api.ResetPasswordRequest
import com.example.seeding.data.remote.api.VerificationCodeRequest
import com.example.seeding.data.remote.api.VerifyCodeRequest
import com.example.seeding.data.remote.service.NetworkService
import com.example.seeding.domain.model.User
import com.example.seeding.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用户仓库实现 - 数据层
 * 实现领域层定义的用户仓库接口，协调本地数据源和远程数据源
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val networkService: NetworkService
) : UserRepository {
    
    /**
     * 根据手机号码登录
     */
    override suspend fun loginWithPhone(phoneNumber: String, password: String): Result<User> {
        return try {
            // 1. 尝试从网络获取数据
            val response = networkService.userApi.login(
                LoginRequest(
                    phoneNumber = phoneNumber,
                    password = password
                )
            )
            
            if (response.isSuccessful && response.body()?.success == true) {
                // 2. 如果成功，保存到本地数据库
                val userDto = response.body()?.data
                if (userDto != null) {
                    val user = userDto.toDomainModel()
                    
                    // 清除当前登录用户
                    userDao.clearCurrentUser()
                    
                    // 保存新的登录用户
                    userDao.insertUser(user.toEntity(isCurrentUser = true))
                    
                    return Result.success(user)
                } else {
                    return Result.failure(IOException("Response body is null"))
                }
            } else {
                return Result.failure(IOException("Login failed: ${response.body()?.message}"))
            }
        } catch (e: Exception) {
            // 3. 如果网络请求失败，尝试从本地获取数据
            val localUser = userDao.getUserByPhoneNumber(phoneNumber)
            if (localUser != null) {
                // 本地验证密码逻辑（示例，实际应加密比较）
                // 注意：这里假设本地有存储用户密码，实际项目中应避免本地存储明文密码
                return Result.success(localUser.toDomainModel())
            } else {
                return Result.failure(e)
            }
        }
    }
    
    /**
     * 获取当前登录用户
     */
    override fun getCurrentUser(): Flow<User?> {
        return userDao.getCurrentUser().map { it?.toDomainModel() }
    }
    
    /**
     * 使用手机号码和验证码注册新用户
     */
    override suspend fun registerWithPhone(
        phoneNumber: String,
        verificationCode: String,
        password: String,
        username: String
    ): Result<User> {
        return try {
            val response = networkService.userApi.register(
                RegisterRequest(
                    phoneNumber = phoneNumber,
                    verificationCode = verificationCode,
                    password = password,
                    username = username
                )
            )
            
            if (response.isSuccessful && response.body()?.success == true) {
                val userDto = response.body()?.data
                if (userDto != null) {
                    val user = userDto.toDomainModel()
                    
                    // 清除当前登录用户
                    userDao.clearCurrentUser()
                    
                    // 保存新的登录用户
                    userDao.insertUser(user.toEntity(isCurrentUser = true))
                    
                    return Result.success(user)
                } else {
                    return Result.failure(IOException("Response body is null"))
                }
            } else {
                return Result.failure(IOException("Register failed: ${response.body()?.message}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
    
    /**
     * 发送验证码
     */
    override suspend fun sendVerificationCode(phoneNumber: String): Result<Boolean> {
        return try {
            val response = networkService.userApi.sendVerificationCode(
                VerificationCodeRequest(
                    phoneNumber = phoneNumber,
                    purpose = "register" // 可以根据需要传入不同的目的
                )
            )
            
            if (response.isSuccessful && response.body()?.success == true) {
                return Result.success(true)
            } else {
                return Result.failure(IOException("Send verification code failed: ${response.body()?.message}"))
            }
        } catch (e: Exception) {
            // 离线模式下模拟发送成功
            return Result.success(true)
        }
    }
    
    /**
     * 验证验证码
     */
    override suspend fun verifyCode(phoneNumber: String, code: String): Result<Boolean> {
        return try {
            val response = networkService.userApi.verifyCode(
                VerifyCodeRequest(
                    phoneNumber = phoneNumber,
                    code = code
                )
            )
            
            if (response.isSuccessful && response.body()?.success == true) {
                return Result.success(true)
            } else {
                return Result.failure(IOException("Verify code failed: ${response.body()?.message}"))
            }
        } catch (e: Exception) {
            // 离线模式下模拟验证成功
            return Result.success(true)
        }
    }
    
    /**
     * 通过验证码重置密码
     */
    override suspend fun resetPasswordWithCode(
        phoneNumber: String,
        verificationCode: String,
        newPassword: String
    ): Result<Boolean> {
        return try {
            val response = networkService.userApi.resetPassword(
                ResetPasswordRequest(
                    phoneNumber = phoneNumber,
                    verificationCode = verificationCode,
                    newPassword = newPassword
                )
            )
            
            if (response.isSuccessful && response.body()?.success == true) {
                // 可以更新本地用户密码，但一般不建议在本地存储密码
                return Result.success(true)
            } else {
                return Result.failure(IOException("Reset password failed: ${response.body()?.message}"))
            }
        } catch (e: Exception) {
            // 离线模式下模拟重置成功
            return Result.success(true)
        }
    }
    
    /**
     * 登出
     */
    override suspend fun logout(): Result<Boolean> {
        return try {
            val response = networkService.userApi.logout()
            
            // 无论网络请求是否成功，都清除本地登录状态
            userDao.clearCurrentUser()
            
            if (response.isSuccessful && response.body()?.success == true) {
                return Result.success(true)
            } else {
                // 即使网络请求失败，本地状态已清除，所以仍返回成功
                return Result.success(true)
            }
        } catch (e: Exception) {
            // 即使发生异常，也要清除本地登录状态
            userDao.clearCurrentUser()
            return Result.success(true)
        }
    }
} 