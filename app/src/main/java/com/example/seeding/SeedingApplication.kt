package com.example.seeding

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import com.example.seeding.util.LanguageManager
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class SeedingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // 初始化语言设置
        LanguageManager.loadLanguageSettings(this)
        updateBaseContextLocale(this)
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