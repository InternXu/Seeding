package com.example.seeding

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.seeding.R
import com.example.seeding.ui.SeedingApp
import com.example.seeding.ui.navigation.Screen
import com.example.seeding.ui.theme.SeedingTheme
import com.example.seeding.util.LanguageManager
import com.example.seeding.ui.theme.AppThemeManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import kotlin.system.exitProcess
import kotlinx.coroutines.launch
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 在 super.onCreate 之前调用 installSplashScreen
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // 加载应用设置
        loadAppSettings()
        
        // 更新当前Context的配置
        updateBaseContextLocale()
        
        setContent {
            SeedingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    // 传递抽屉状态给SeedingApp，并获取返回值
                    val (drawerState, app) = SeedingApp(navController = navController)
                    
                    // 处理主页面的返回事件，实现双击返回退出应用，同时处理抽屉菜单关闭
                    HandleDoubleBackToExit(navController, drawerState)
                    
                    app()
                }
            }
        }
    }
    
    /**
     * 加载所有应用设置
     */
    private fun loadAppSettings() {
        // 加载语言设置
        LanguageManager.loadLanguageSettings(this)
        
        // 加载主题设置
        AppThemeManager.loadThemeSettings(this)
    }
    
    /**
     * 确保基础Context使用正确的语言配置
     */
    private fun updateBaseContextLocale() {
        val languageCode = LanguageManager.getCurrentLanguageCode()
        val locale = when {
            languageCode.startsWith("zh-CN") -> Locale.SIMPLIFIED_CHINESE
            languageCode.startsWith("zh-TW") -> Locale.TRADITIONAL_CHINESE
            languageCode.startsWith("en") -> Locale.ENGLISH
            languageCode.startsWith("ja") -> Locale.JAPANESE
            else -> Locale.getDefault()
        }
        
        val configuration = Configuration(resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            configuration.setLocales(localeList)
        } else {
            configuration.locale = locale
        }
        
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
    
    /**
     * 处理配置变更，确保语言设置在配置变更后仍然生效
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateBaseContextLocale()
    }
    
    /**
     * 覆盖attachBaseContext方法，确保在Activity创建时使用正确的语言
     */
    override fun attachBaseContext(newBase: Context) {
        // 先获取默认设置
        super.attachBaseContext(newBase)
        
        val languageCode = LanguageManager.getCurrentLanguageCode()
        val locale = when {
            languageCode.startsWith("zh-CN") -> Locale.SIMPLIFIED_CHINESE
            languageCode.startsWith("zh-TW") -> Locale.TRADITIONAL_CHINESE
            languageCode.startsWith("en") -> Locale.ENGLISH
            languageCode.startsWith("ja") -> Locale.JAPANESE
            else -> Locale.getDefault()
        }
        
        val configuration = Configuration(newBase.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            configuration.setLocales(localeList)
        } else {
            configuration.locale = locale
        }
        
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}

/**
 * 处理主页面的返回事件
 * 当用户在三个主页面中按返回键时：
 * 1. 如果抽屉菜单打开，则先关闭抽屉菜单
 * 2. 如果抽屉菜单已关闭，实现双击返回退出功能
 */
@Composable
private fun HandleDoubleBackToExit(navController: NavHostController, drawerState: DrawerState) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // 主页面路由列表
    val mainScreens = listOf(Screen.Action.route, Screen.Goal.route, Screen.Harvest.route)
    
    // 如果当前在主页面，实现返回键逻辑
    if (currentRoute in mainScreens) {
        val context = LocalContext.current
        var lastBackPressed by remember { mutableStateOf(0L) }
        val scope = rememberCoroutineScope()
        
        BackHandler(enabled = true) {
            // 检查抽屉菜单是否打开
            if (drawerState.isOpen) {
                // 如果抽屉菜单打开，先关闭抽屉菜单
                scope.launch {
                    drawerState.close()
                }
            } else {
                // 抽屉菜单已关闭，执行双击返回退出逻辑
                val currentTime = System.currentTimeMillis()
                
                if (currentTime - lastBackPressed < 2000) { // 两秒内连续按两次返回键
                    // 结束所有Activity并退出应用
                    android.os.Process.killProcess(android.os.Process.myPid())
                    exitProcess(0)
                } else {
                    // 显示提示，并记录此次按键时间
                    Toast.makeText(context, context.getString(R.string.double_back_exit), Toast.LENGTH_SHORT).show()
                    lastBackPressed = currentTime
                }
            }
        }
    }
} 