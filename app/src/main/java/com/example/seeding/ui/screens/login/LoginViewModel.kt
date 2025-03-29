package com.example.seeding.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seeding.data.model.User
import com.example.seeding.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * 登录状态
 */
sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val userId: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val loginState: StateFlow<LoginUiState> = _loginState
    
    /**
     * 登录
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            
            try {
                // TODO: 实现真正的登录认证逻辑，与后端API交互
                // 临时实现：检查本地数据库中是否有此邮箱账号
                val user = userRepository.getUserByEmail(email)
                
                if (user != null) {
                    // 找到用户，更新登录时间并返回成功
                    userRepository.updateLastLogin(user.userId)
                    _loginState.value = LoginUiState.Success(user.userId)
                } else {
                    // 用户不存在
                    _loginState.value = LoginUiState.Error("用户不存在或密码错误")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error(e.message ?: "登录失败")
            }
        }
    }
    
    /**
     * 注册新用户
     */
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            
            try {
                // 检查邮箱是否已被注册
                val existingUser = userRepository.getUserByEmail(email)
                
                if (existingUser != null) {
                    _loginState.value = LoginUiState.Error("该邮箱已被注册")
                    return@launch
                }
                
                // 创建新用户
                val userId = UUID.randomUUID().toString()
                val newUser = User(
                    userId = userId,
                    username = username,
                    email = email
                    // 密码应该被加密，这里简化处理，实际应用中需要加密存储
                )
                
                userRepository.insertUser(newUser)
                _loginState.value = LoginUiState.Success(userId)
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error(e.message ?: "注册失败")
            }
        }
    }
} 