package com.example.seeding.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.example.seeding.ui.screens.store.StoreScreen

/**
 * 应用程序的导航目标
 */
sealed class Screen(val route: String) {
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

// 动画持续时间
private const val ANIM_DURATION = 300

// 主页面路由列表
val mainScreenRoutes = listOf(
    Screen.Action.route,
    Screen.Goal.route,
    Screen.Harvest.route
)

// 抽屉菜单页面路由列表
val drawerScreenRoutes = listOf(
    Screen.Profile.route,
    Screen.Settings.route,
    Screen.Store.route
)

// 设置子页面路由列表
val settingsSubScreenRoutes = listOf(
    Screen.SettingsLanguage.route,
    Screen.SettingsDisplay.route
)

/**
 * 导航方向枚举
 */
enum class NavigationDirection {
    LEFT, RIGHT, FORWARD
}

/**
 * 确定两个目标之间应该使用哪种导航方向
 */
private fun determineDirection(sourceRoute: String?, targetRoute: String?): NavigationDirection {
    if (sourceRoute == null || targetRoute == null) return NavigationDirection.FORWARD
    
    // 如果源和目标都是主页面，判断它们的索引位置决定方向
    if (sourceRoute in mainScreenRoutes && targetRoute in mainScreenRoutes) {
        val sourceIndex = mainScreenRoutes.indexOf(sourceRoute)
        val targetIndex = mainScreenRoutes.indexOf(targetRoute)
        
        return if (targetIndex > sourceIndex) {
            NavigationDirection.RIGHT
        } else {
            NavigationDirection.LEFT
        }
    }
    
    // 如果从主页面进入抽屉菜单页面，从右边进入
    if (sourceRoute in mainScreenRoutes && targetRoute in drawerScreenRoutes) {
        return NavigationDirection.RIGHT
    }
    
    // 如果从抽屉菜单页面返回主页面，从左边进入
    if (sourceRoute in drawerScreenRoutes && targetRoute in mainScreenRoutes) {
        return NavigationDirection.LEFT
    }
    
    // 如果从设置页面进入子页面，从右边进入
    if (sourceRoute == Screen.Settings.route && targetRoute in settingsSubScreenRoutes) {
        return NavigationDirection.RIGHT
    }
    
    // 如果从设置子页面返回设置页面，从左边进入
    if (sourceRoute in settingsSubScreenRoutes && targetRoute == Screen.Settings.route) {
        return NavigationDirection.LEFT
    }
    
    // 默认前进方向
    return NavigationDirection.FORWARD
}

/**
 * 主导航组件
 */
@Composable
fun SeedingNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.Login.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, 
                    animationSpec = tween(ANIM_DURATION))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(ANIM_DURATION))
            }
        ) {
            LoginScreen(navController = navController)
        }
        
        composable(
            route = Screen.Register.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, 
                    animationSpec = tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, 
                    animationSpec = tween(ANIM_DURATION))
            }
        ) {
            RegisterScreen(navController = navController)
        }
        
        composable(
            route = Screen.ForgotPassword.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, 
                    animationSpec = tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, 
                    animationSpec = tween(ANIM_DURATION))
            }
        ) {
            ForgotPasswordScreen(navController = navController)
        }
        
        // 主界面路由 - 播种页面
        composable(
            route = Screen.Action.route,
            enterTransition = {
                val sourceRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                val direction = determineDirection(sourceRoute, targetRoute)
                
                when (direction) {
                    NavigationDirection.RIGHT -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    NavigationDirection.LEFT -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    else -> fadeIn(animationSpec = tween(ANIM_DURATION))
                }
            },
            exitTransition = {
                val sourceRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                val direction = determineDirection(sourceRoute, targetRoute)
                
                when (direction) {
                    NavigationDirection.RIGHT -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    NavigationDirection.LEFT -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    else -> fadeOut(animationSpec = tween(ANIM_DURATION))
                }
            },
            popEnterTransition = {
                val sourceRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                val direction = determineDirection(targetRoute, sourceRoute)
                
                when (direction) {
                    NavigationDirection.RIGHT -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    NavigationDirection.LEFT -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    else -> fadeIn(animationSpec = tween(ANIM_DURATION))
                }
            },
            popExitTransition = {
                val sourceRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                val direction = determineDirection(targetRoute, sourceRoute)
                
                when (direction) {
                    NavigationDirection.RIGHT -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    NavigationDirection.LEFT -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    else -> fadeOut(animationSpec = tween(ANIM_DURATION))
                }
            }
        ) {
            ActionScreen(navController = navController)
        }
        
        // 目标页面
        composable(
            route = Screen.Goal.route,
            enterTransition = {
                val sourceRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                val direction = determineDirection(sourceRoute, targetRoute)
                
                when (direction) {
                    NavigationDirection.RIGHT -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    NavigationDirection.LEFT -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    else -> fadeIn(animationSpec = tween(ANIM_DURATION))
                }
            },
            exitTransition = {
                val sourceRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                val direction = determineDirection(sourceRoute, targetRoute)
                
                when (direction) {
                    NavigationDirection.RIGHT -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    NavigationDirection.LEFT -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    else -> fadeOut(animationSpec = tween(ANIM_DURATION))
                }
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(ANIM_DURATION))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(ANIM_DURATION))
            }
        ) {
            GoalScreen(navController = navController)
        }
        
        // 收获页面
        composable(
            route = Screen.Harvest.route,
            enterTransition = {
                val sourceRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                val direction = determineDirection(sourceRoute, targetRoute)
                
                when (direction) {
                    NavigationDirection.RIGHT -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    NavigationDirection.LEFT -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    else -> fadeIn(animationSpec = tween(ANIM_DURATION))
                }
            },
            exitTransition = {
                val sourceRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                val direction = determineDirection(sourceRoute, targetRoute)
                
                when (direction) {
                    NavigationDirection.RIGHT -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    NavigationDirection.LEFT -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIM_DURATION)
                    )
                    else -> fadeOut(animationSpec = tween(ANIM_DURATION))
                }
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(ANIM_DURATION))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(ANIM_DURATION))
            }
        ) {
            HarvestScreen(navController = navController)
        }
        
        // 抽屉菜单路由 - 个人页面
        composable(
            route = Screen.Profile.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, 
                    animationSpec = tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, 
                    animationSpec = tween(ANIM_DURATION))
            }
        ) {
            ProfileScreen(navController = navController)
        }
        
        // 设置页面
        composable(
            route = Screen.Settings.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, 
                    animationSpec = tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, 
                    animationSpec = tween(ANIM_DURATION))
            }
        ) {
            SettingsScreen(navController = navController)
        }
        
        // 设置子页面 - 语言设置
        composable(
            route = Screen.SettingsLanguage.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, 
                    animationSpec = tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, 
                    animationSpec = tween(ANIM_DURATION))
            }
        ) {
            SettingsScreen(navController = navController)
        }
        
        // 设置子页面 - 显示设置
        composable(
            route = Screen.SettingsDisplay.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, 
                    animationSpec = tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, 
                    animationSpec = tween(ANIM_DURATION))
            }
        ) {
            SettingsScreen(navController = navController)
        }
        
        // 商店页面
        composable(
            route = Screen.Store.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, 
                    animationSpec = tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, 
                    animationSpec = tween(ANIM_DURATION))
            }
        ) {
            StoreScreen(navController = navController)
        }
        
        // TODO: 添加详情页面路由
    }
} 