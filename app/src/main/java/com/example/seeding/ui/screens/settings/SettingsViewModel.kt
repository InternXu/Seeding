package com.example.seeding.ui.screens.settings

import android.app.Application
import android.app.Activity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seeding.ui.theme.ThemeType
import com.example.seeding.ui.theme.FontType
import com.example.seeding.util.LanguageManager
import com.example.seeding.ui.theme.AppThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 设置页面的ViewModel
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {
    
    // UI状态
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        // 初始化语言显示
        _uiState.update { 
            it.copy(language = LanguageManager.getCurrentLanguageName()) 
        }
    }
    
    // 更新语言状态但不触发语言切换（用于外部语言变化同步到ViewModel）
    fun updateLanguageNoSwitch(language: String) {
        _uiState.update { it.copy(language = language) }
    }
    
    // 更新语言
    fun updateLanguage(language: String) {
        if (language == _uiState.value.language) return
        
        _uiState.update { it.copy(language = language) }
        
        // 使用新的LanguageManager切换语言，这会触发UI重组
        LanguageManager.switchLanguage(language, application)
        // 保存语言设置
        LanguageManager.saveLanguageSettings(application)
    }
    
    // 更新主题类型
    fun updateThemeType(themeType: ThemeType) {
        _uiState.update { it.copy(themeType = themeType) }
        // 立即应用主题变更
        AppThemeManager.updateThemeType(themeType)
    }
    
    // 更新深色模式
    fun updateDarkMode(isDarkMode: Boolean) {
        _uiState.update { it.copy(isDarkMode = isDarkMode) }
        // 立即应用深色模式变更
        AppThemeManager.updateDarkMode(isDarkMode)
    }
    
    // 更新字体大小
    fun updateFontSize(fontSize: Float) {
        _uiState.update { it.copy(fontSize = fontSize) }
        // 立即应用字体大小变更
        AppThemeManager.updateFontSize(fontSize)
    }
    
    // 清除错误消息
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

/**
 * 设置页面UI状态数据类
 */
data class SettingsUiState(
    val language: String = LanguageManager.getCurrentLanguageName(),
    val themeType: ThemeType = ThemeType.DEFAULT,
    val isDarkMode: Boolean = false,
    val fontSize: Float = 1.0f,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) 