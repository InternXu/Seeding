package com.example.seeding

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.util.Log
import com.example.seeding.domain.repository.SeedRepository
import com.example.seeding.util.LanguageManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class SeedingApplication : Application() {

    @Inject
    lateinit var seedRepository: SeedRepository
    
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
            } catch (e: Exception) {
                Log.e("SeedingApplication", "初始化数据失败: ${e.message}", e)
                // 记录详细的异常栈，帮助调试
                e.printStackTrace()
            }
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