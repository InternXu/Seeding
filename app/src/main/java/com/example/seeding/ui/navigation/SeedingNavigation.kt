package com.example.seeding.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.seeding.ui.screens.action.ActionScreen
import com.example.seeding.ui.screens.goal.GoalScreen
import com.example.seeding.ui.screens.harvest.HarvestScreen
import com.example.seeding.ui.screens.login.ForgotPasswordScreen
import com.example.seeding.ui.screens.login.LoginScreen
import com.example.seeding.ui.screens.login.RegisterScreen
import com.example.seeding.ui.screens.profile.ProfileScreen
import com.example.seeding.ui.screens.settings.SettingsScreen
import com.example.seeding.ui.screens.splash.SplashScreen
import com.example.seeding.ui.screens.store.StoreScreen

/**
 * 应用程序的导航目标
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Action : Screen("action")
    object Goal : Screen("goal")
    object Harvest : Screen("harvest")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Store : Screen("store")
    
    // 详情页面
    object ActionDetail : Screen("action/{actionId}") {
        fun createRoute(actionId: String) = "action/$actionId"
    }
    
    object GoalDetail : Screen("goal/{goalId}") {
        fun createRoute(goalId: String) = "goal/$goalId"
    }
    
    object SeedDetail : Screen("seed/{seedId}") {
        fun createRoute(seedId: Int) = "seed/$seedId"
    }

    // 设置子页面
    object SettingsLanguage : Screen("settings/language")
    object SettingsDisplay : Screen("settings/display")
}

/**
 * 主导航组件
 */
@Composable
fun SeedingNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }
        
        // 主界面路由 - 这些路由是平等的
        composable(Screen.Action.route) {
            ActionScreen(navController = navController)
        }
        
        composable(Screen.Goal.route) {
            GoalScreen(navController = navController)
        }
        
        composable(Screen.Harvest.route) {
            HarvestScreen(navController = navController)
        }
        
        // 抽屉菜单路由
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        
        // 设置子页面路由 - 也指向SettingsScreen，但会有不同的标题
        composable(Screen.SettingsLanguage.route) {
            SettingsScreen(navController = navController)
        }
        
        composable(Screen.SettingsDisplay.route) {
            SettingsScreen(navController = navController)
        }
        
        composable(Screen.Store.route) {
            StoreScreen(navController = navController)
        }
        
        // TODO: 添加详情页面路由
    }
} 