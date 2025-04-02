package com.example.seeding

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.util.Log
import com.example.seeding.domain.model.User
import com.example.seeding.domain.repository.SeedRepository
import com.example.seeding.domain.repository.UserRepository
import com.example.seeding.util.LanguageManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltAndroidApp
class SeedingApplication : Application() {

    @Inject
    lateinit var seedRepository: SeedRepository
    
    @Inject
    lateinit var userRepository: UserRepository
    
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    override fun onCreate() {
        super.onCreate()
        
        // 初始化语言设置
        LanguageManager.loadLanguageSettings(this)
        updateBaseContextLocale(this)
        
        // 初始化应用数据
        initializeData()
    }
    
    private fun initializeData() {
        applicationScope.launch(Dispatchers.IO) {
            try {
                // 初始化种子数据
                seedRepository.initializeSeeds()
                Log.d("SeedingApplication", "应用数据初始化完成")
                
                // 确保存在一个默认用户，为跳过登录做准备
                createDefaultUserIfNeeded()
            } catch (e: Exception) {
                Log.e("SeedingApplication", "初始化数据失败: ${e.message}", e)
                // 记录详细的异常栈，帮助调试
                e.printStackTrace()
            }
        }
    }
    
    private suspend fun createDefaultUserIfNeeded() {
        try {
            // 检查是否已有当前用户
            val currentUser = userRepository.getCurrentUser().first()
            if (currentUser == null) {
                // 没有当前用户，创建一个默认用户
                val defaultUserId = UUID.randomUUID().toString()
                val defaultUser = User(
                    id = defaultUserId,
                    username = "默认用户",
                    phoneNumber = "13800138000"
                )
                
                userRepository.saveUser(defaultUser)
                userRepository.setCurrentUser(defaultUserId)
                Timber.d("已创建默认用户: ${defaultUser.username}")
            } else {
                Timber.d("当前用户已存在: ${currentUser.username}")
            }
        } catch (e: Exception) {
            Timber.e(e, "创建默认用户失败")
        }
    }
    
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        updateBaseContextLocale(base)
    }
    
    private fun updateBaseContextLocale(context: Context) {
        val languageCode = LanguageManager.getCurrentLanguageCode()
        val locale = when {
            languageCode.startsWith("zh-CN") -> Locale.SIMPLIFIED_CHINESE
            languageCode.startsWith("zh-TW") -> Locale.TRADITIONAL_CHINESE
            languageCode.startsWith("en") -> Locale.ENGLISH
            languageCode.startsWith("ja") -> Locale.JAPANESE
            else -> Locale.getDefault()
        }
        
        Locale.setDefault(locale)
        
        val configuration = context.resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        } else {
            configuration.locale = locale
        }
        
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }
} 