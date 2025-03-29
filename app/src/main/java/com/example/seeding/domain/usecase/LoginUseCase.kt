package com.example.seeding.domain.usecase

import com.example.seeding.domain.model.User
import com.example.seeding.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 登录用例 - 领域层
 * 封装登录相关的业务逻辑
 */
class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * 使用手机号码和密码登录
     */
    suspend operator fun invoke(phoneNumber: String, password: String): Result<User> {
        // 这里可以添加验证逻辑，例如检查手机号格式、密码强度等
        if (phoneNumber.isEmpty()) {
            return Result.failure(IllegalArgumentException("手机号不能为空"))
        }
        
        if (password.isEmpty()) {
            return Result.failure(IllegalArgumentException("密码不能为空"))
        }
        
        return userRepository.loginWithPhone(phoneNumber, password)
    }
} 