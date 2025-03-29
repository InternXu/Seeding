package com.example.seeding.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import java.util.Locale

// 语言代码常量
const val LANGUAGE_CHINESE_SIMPLIFIED = "zh-CN" // 简体中文
const val LANGUAGE_CHINESE_TRADITIONAL = "zh-TW" // 繁体中文
const val LANGUAGE_ENGLISH = "en" // 英语
const val LANGUAGE_JAPANESE = "ja" // 日语

// 创建一个CompositionLocal来提供当前语言设置
val LocalLanguageState = staticCompositionLocalOf { LanguageState() }

// 语言状态类
class LanguageState {
    private val _currentLanguageCode: MutableState<String> = mutableStateOf(LANGUAGE_CHINESE_SIMPLIFIED)
    val currentLanguageCode: State<String> = _currentLanguageCode
    
    // 用于强制整个应用重组的状态
    private val _forceUpdate = mutableStateOf(0)
    val forceUpdate: State<Int> = _forceUpdate
    
    // 更新语言代码并触发重组
    fun updateLanguageCode(code: String) {
        _currentLanguageCode.value = code
        // 增加forceUpdate计数，触发整个应用重组
        _forceUpdate.value += 1
    }
}

/**
 * 语言管理工具类，负责应用的多语言切换和持久化
 */
object LanguageManager {
    // 语言显示名称到语言代码的映射
    private val languageNameToCode = mapOf(
        "简体中文" to LANGUAGE_CHINESE_SIMPLIFIED,
        "繁體中文" to LANGUAGE_CHINESE_TRADITIONAL,
        "English" to LANGUAGE_ENGLISH,
        "日本語" to LANGUAGE_JAPANESE
    )
    
    // 语言代码到语言显示名称的映射
    private val languageCodeToName = mapOf(
        LANGUAGE_CHINESE_SIMPLIFIED to "简体中文",
        LANGUAGE_CHINESE_TRADITIONAL to "繁體中文",
        LANGUAGE_ENGLISH to "English",
        LANGUAGE_JAPANESE to "日本語"
    )
    
    // 全局语言状态
    val languageState = LanguageState()
    
    /**
     * 根据语言名称切换应用语言
     * @param languageName 语言显示名称
     * @param context 上下文，用于更新资源
     * @return 是否切换成功
     */
    fun switchLanguage(languageName: String, context: Context? = null): Boolean {
        val languageCode = languageNameToCode[languageName] ?: return false
        
        // 更新语言代码，这会触发使用LocalLanguageState的组件重组
        languageState.updateLanguageCode(languageCode)
        
        // 使用AppCompatDelegate设置系统语言（可能不会立即生效）
        val localeList = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(localeList)
        
        // 如果提供了Context，则立即更新资源配置
        context?.let { updateResources(it, languageCode) }
        
        return true
    }
    
    /**
     * 更新应用程序的语言资源
     * @param context 上下文
     * @param languageCode 语言代码
     */
    private fun updateResources(context: Context, languageCode: String) {
        // 创建区域设置对象
        val locale = when {
            languageCode.startsWith("zh-CN") -> Locale.SIMPLIFIED_CHINESE
            languageCode.startsWith("zh-TW") -> Locale.TRADITIONAL_CHINESE
            languageCode.startsWith("en") -> Locale.ENGLISH
            languageCode.startsWith("ja") -> Locale.JAPANESE
            else -> Locale.getDefault()
        }
        
        Locale.setDefault(locale)
        
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        } else {
            configuration.locale = locale
        }
        
        // 应用配置
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
    
    /**
     * 获取当前语言名称
     * @return 当前语言显示名称
     */
    fun getCurrentLanguageName(): String {
        return languageCodeToName[languageState.currentLanguageCode.value] ?: "简体中文"
    }
    
    /**
     * 获取当前语言代码
     * @return 当前语言代码
     */
    fun getCurrentLanguageCode(): String {
        return languageState.currentLanguageCode.value
    }
    
    /**
     * 保存语言设置到 SharedPreferences
     * @param context 上下文
     */
    fun saveLanguageSettings(context: Context) {
        val prefs = getSharedPreferences(context)
        prefs.edit().putString("language_code", languageState.currentLanguageCode.value).apply()
    }
    
    /**
     * 从 SharedPreferences 加载语言设置
     * @param context 上下文
     * @return 是否成功加载设置
     */
    fun loadLanguageSettings(context: Context): Boolean {
        val prefs = getSharedPreferences(context)
        val savedLanguageCode = prefs.getString("language_code", null) ?: return false
        
        // 更新语言状态
        languageState.updateLanguageCode(savedLanguageCode)
        
        // 立即更新资源配置
        updateResources(context, savedLanguageCode)
        
        // 使用AppCompatDelegate设置系统语言
        val localeList = LocaleListCompat.forLanguageTags(savedLanguageCode)
        AppCompatDelegate.setApplicationLocales(localeList)
        
        return true
    }
    
    /**
     * 获取 SharedPreferences
     */
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    }
}

/**
 * 强制刷新资源的Composable
 */
@Composable
fun LocaleAwareComposable(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val languageState = LocalLanguageState.current
    val forceUpdate = languageState.forceUpdate.value // 获取强制更新的值
    
    // 当语言变更时，强制更新Context的配置
    val languageCode = languageState.currentLanguageCode.value
    
    // 每次forceUpdate值变化时，重新应用语言配置
    val locale = remember(languageCode, forceUpdate) {
        when {
            languageCode.startsWith("zh-CN") -> Locale.SIMPLIFIED_CHINESE
            languageCode.startsWith("zh-TW") -> Locale.TRADITIONAL_CHINESE
            languageCode.startsWith("en") -> Locale.ENGLISH
            languageCode.startsWith("ja") -> Locale.JAPANESE
            else -> Locale.getDefault()
        }
    }
    
    // 使用新的Locale配置应用UI
    val configuration = LocalConfiguration.current
    val updatedConfiguration = remember(configuration, locale, forceUpdate) {
        Configuration(configuration).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setLocales(LocaleList(locale))
            } else {
                this.locale = locale
            }
        }
    }
    
    // 重新应用资源配置
    context.resources.updateConfiguration(updatedConfiguration, context.resources.displayMetrics)
    
    // 提供内容
    content()
}

/**
 * 提供语言设置的Composable
 */
@Composable
fun ProvideLanguageSettings(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalLanguageState provides LanguageManager.languageState
    ) {
        LocaleAwareComposable {
            content()
        }
    }
} 