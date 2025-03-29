# Seeding应用项目说明文档

## 1. 项目架构概述

### 1.1 技术栈
- **开发语言**：Kotlin
- **UI框架**：Jetpack Compose
- **架构模式**：MVVM (Model-View-ViewModel)
- **依赖注入**：Hilt
- **导航**：Jetpack Navigation Compose
- **状态管理**：StateFlow, CompositionLocal
- **持久化存储**：SharedPreferences

### 1.2 项目结构
```
com.example.seeding/
├── ui/                      # UI相关组件
│   ├── components/          # 通用UI组件
│   ├── navigation/          # 导航相关
│   ├── screens/             # 各个页面
│   │   ├── action/          # 播种页面
│   │   ├── goal/            # 目标页面
│   │   ├── harvest/         # 收获页面
│   │   ├── login/           # 登录相关页面
│   │   ├── profile/         # 个人资料页面
│   │   ├── settings/        # 设置页面
│   │   ├── splash/          # 启动页
│   │   └── store/           # 商店页面
│   ├── theme/               # 主题相关
│   └── SeedingApp.kt        # 应用主入口Composable
├── util/                    # 工具类
│   ├── LanguageManager.kt   # 语言管理器
│   └── ...
├── MainActivity.kt          # 主Activity
└── SeedingApplication.kt    # Application类
```

### 1.3 架构设计

项目采用MVVM架构，主要组件：
- **Model**：数据模型和仓库层
- **View**：Compose UI组件
- **ViewModel**：负责业务逻辑和状态管理

状态管理采用单向数据流：
- ViewModel通过StateFlow提供UI状态
- UI层观察状态并显示
- 用户操作通过ViewModel方法传递

全局状态管理采用CompositionLocal：
- 语言状态：`LocalLanguageState`
- 主题状态：通过`AppThemeManager`

## 2. 页面逻辑详解

### 2.1 导航系统

导航系统基于Jetpack Navigation Compose实现，定义在`SeedingNavigation.kt`中：

```kotlin
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
    
    // 详情页和设置子页面
    object ActionDetail : Screen("action/{actionId}")
    object GoalDetail : Screen("goal/{goalId}")
    object SeedDetail : Screen("seed/{seedId}")
    object SettingsLanguage : Screen("settings/language")
    object SettingsDisplay : Screen("settings/display")
}
```

导航逻辑分为三层：
1. **底部导航栏**：Action、Goal、Harvest页面间导航
2. **抽屉菜单**：Profile、Settings、Store页面导航
3. **页面内导航**：如Settings内部的子导航

### 2.2 主要页面

#### 2.2.1 SeedingApp

`SeedingApp.kt`是整个应用的UI入口，包含：
- 顶部应用栏
- 抽屉菜单
- 底部导航栏
- 内容区域

关键逻辑：
- 通过`ProvideLanguageSettings`提供语言环境
- 使用`MaterialTheme`提供主题
- 监听导航变化，更新UI状态
- 处理返回键逻辑

#### 2.2.2 设置页面

`SettingsScreen.kt`实现设置页面，包含三个子页面：
- **主设置页**：显示设置选项列表
- **语言设置页**：选择应用语言
- **显示设置页**：主题、深色模式、字体大小设置

设置页使用`SettingsViewModel`管理状态：
```kotlin
data class SettingsUiState(
    val language: String = LanguageManager.getCurrentLanguageName(),
    val themeType: ThemeType = ThemeType.DEFAULT,
    val isDarkMode: Boolean = false,
    val fontSize: Float = 1.0f,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
```

关键逻辑：
- 通过`currentPage`状态管理子页面切换
- `LaunchedEffect`监听路由变化，同步页面状态
- `LaunchedEffect(forceUpdate)`监听语言变化，更新UI
- `BackHandler`处理返回逻辑，确保返回主设置页而非退出

### 2.3 认证页面
包含登录、注册和忘记密码页面，均使用`LoginViewModel`管理状态。

### 2.4 主功能页面
- **Action(播种)页面**：用户主要操作界面
- **Goal(目标)页面**：目标管理界面
- **Harvest(收获)页面**：成果展示界面

## 3. 控件实现详解

### 3.1 顶部应用栏

`SeedingTopAppBar.kt`实现自定义顶部应用栏：
```kotlin
@Composable
fun SeedingTopAppBar(
    currentRoute: String?,
    isDrawerScreen: Boolean = false,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit,
    actions: @Composable (() -> Unit)? = null
)
```

特点：
- 根据当前路由显示不同标题
- 区分抽屉页面和主页面，显示不同导航图标
- 支持自定义操作按钮
- 监听语言变化，确保标题文本实时更新

### 3.2 设置页面组件

#### 3.2.1 设置项

```kotlin
@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
)
```
用于主设置页的入口项，包含图标、标题、副标题和点击操作。

#### 3.2.2 开关选项

```kotlin
@Composable
fun SettingsSwitchItem(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
)
```
用于开关类设置，如深色模式。根据当前主题调整颜色。

#### 3.2.3 单选按钮选项

```kotlin
@Composable
fun SettingsRadioItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
)
```
用于单选类设置，如语言选择。

#### 3.2.4 主题颜色选择项

```kotlin
@Composable
fun ThemeColorItem(
    name: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
)
```
用于主题颜色选择，显示颜色圆圈和名称，选中时显示对勾图标。

### 3.3 抽屉菜单与底部导航栏

在`SeedingApp.kt`中实现：
- 抽屉菜单使用`ModalNavigationDrawer`
- 底部导航栏使用`NavigationBar`和`NavigationBarItem`
- 通过数据模型定义导航项：`BottomNavItem`和`DrawerItem`

## 4. 主题与语言系统

### 4.1 主题系统

#### 4.1.1 主题管理器

`AppThemeManager.kt`是全局单例，负责管理主题状态：
```kotlin
object AppThemeManager {
    // 当前主题类型
    var currentThemeType by mutableStateOf(ThemeType.DEFAULT)
        private set
    
    // 是否深色模式
    var isDarkMode by mutableStateOf(false)
        private set
    
    // 字体大小缩放
    var fontSizeScale by mutableStateOf(1.0f)
        private set
    
    // 更新主题、深色模式和字体大小的方法
    fun updateThemeType(themeType: ThemeType) {...}
    fun updateDarkMode(isDarkMode: Boolean) {...}
    fun updateFontSize(scale: Float) {...}
    
    // 获取当前主题的颜色方案
    fun getColorScheme(): ColorScheme {...}
}
```

#### 4.1.2 主题类型

```kotlin
enum class ThemeType {
    DEFAULT,   // 默认紫色
    NATURE,    // 自然绿色
    OCEAN,     // 海洋蓝色
    VITALITY,  // 活力橙色
    FLORAL,    // 花卉紫色
    RETRO,     // 复古灰色
    HOMELAND,  // 祖国红色
    MINIMAL,   // 极简风格
    SKY        // 天空蓝色
}
```

#### 4.1.3 主题应用

在`SeedingApp.kt`中应用主题：
```kotlin
MaterialTheme(
    colorScheme = AppThemeManager.getColorScheme(),
    typography = Typography.copy(
        // 根据fontSizeScale调整字体大小
    )
) {
    // 应用内容
}
```

### 4.2 语言系统

#### 4.2.1 语言管理器

`LanguageManager.kt`实现多语言支持：
```kotlin
object LanguageManager {
    // 语言代码常量
    val languageState = LanguageState()
    
    // 语言切换方法
    fun switchLanguage(languageName: String, context: Context? = null): Boolean {...}
    
    // 获取/保存语言设置
    fun getCurrentLanguageName(): String {...}
    fun getCurrentLanguageCode(): String {...}
    fun saveLanguageSettings(context: Context) {...}
    fun loadLanguageSettings(context: Context): Boolean {...}
    
    // 更新资源配置
    private fun updateResources(context: Context, languageCode: String) {...}
}
```

#### 4.2.2 语言状态

使用CompositionLocal传递语言状态：
```kotlin
// 创建CompositionLocal提供语言状态
val LocalLanguageState = staticCompositionLocalOf { LanguageState() }

// 语言状态类
class LanguageState {
    private val _currentLanguageCode: MutableState<String> = mutableStateOf(LANGUAGE_CHINESE_SIMPLIFIED)
    val currentLanguageCode: State<String> = _currentLanguageCode
    
    // 用于触发UI重组的计数器
    private val _forceUpdate = mutableStateOf(0)
    val forceUpdate: State<Int> = _forceUpdate
    
    fun updateLanguageCode(code: String) {
        _currentLanguageCode.value = code
        _forceUpdate.value += 1  // 增加计数触发重组
    }
}
```

#### 4.2.3 语言应用

在UI中应用语言设置：
```kotlin
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

@Composable
fun LocaleAwareComposable(content: @Composable () -> Unit) {
    // 监听语言变化，更新资源配置
    val context = LocalContext.current
    val languageState = LocalLanguageState.current
    val forceUpdate = languageState.forceUpdate.value
    
    // 当语言变更时，更新配置
    val locale = remember(languageState.currentLanguageCode.value, forceUpdate) {
        // 根据语言代码创建Locale
    }
    
    // 更新资源配置
    context.resources.updateConfiguration(...)
    
    content()
}
``` 