# Seeding应用开发指南

## 5. 关键实现细节

### 5.1 即时主题切换

主题切换通过以下步骤实现：
1. `SettingsScreen`中用户选择新主题
2. `SettingsViewModel`调用`updateThemeType`更新ViewModel状态
3. 调用`AppThemeManager.updateThemeType`更新全局主题状态
4. 由于使用了`mutableStateOf`，状态变化自动触发UI重组
5. `MaterialTheme`重新计算颜色方案并应用

### 5.2 即时语言切换

语言切换比主题更复杂，涉及Android资源系统：
1. `SettingsScreen`中用户选择新语言
2. `SettingsViewModel`调用`updateLanguage`更新ViewModel状态
3. 调用`LanguageManager.switchLanguage`更新语言状态
4. `languageState.updateLanguageCode`递增`forceUpdate`计数器
5. `LocaleAwareComposable`检测到变化，更新资源配置
6. `SeedingTopAppBar`等组件监听`forceUpdate`，触发UI重组
7. `stringResource`函数重新获取对应语言的字符串资源

关键点是`languageState.forceUpdate`计数器，它确保了语言变化能触发整个UI树的重组。

### 5.3 页面状态保持

设置页面使用`remember`和`rememberSaveable`保持状态：
```kotlin
var currentPage by remember { mutableStateOf(SettingsPage.MAIN) }
```

页面内部导航使用`LaunchedEffect`同步路由和状态：
```kotlin
LaunchedEffect(currentPage) {
    val targetRoute = when(currentPage) {
        SettingsPage.MAIN -> Screen.Settings.route
        SettingsPage.LANGUAGE -> Screen.SettingsLanguage.route
        SettingsPage.DISPLAY -> Screen.SettingsDisplay.route
    }
    
    // 更新导航状态
}
```

## 6. 详细开发指南

### 6.1 新增主题

#### 6.1.1 步骤详解

1. **添加主题类型**：在`ThemeType.kt`中添加新的枚举值
   ```kotlin
   enum class ThemeType {
       DEFAULT,
       NATURE,
       // ... 现有主题
       NEW_THEME // 添加新主题
   }
   ```

2. **定义颜色方案**：在`AppThemeManager.kt`的`getColorScheme()`方法中添加新主题的颜色定义
   ```kotlin
   fun getColorScheme(): ColorScheme {
       return when (currentThemeType) {
           // ... 现有主题配置
           ThemeType.NEW_THEME -> {
               if (isDarkMode) {
                   // 深色模式配色
                   darkColorScheme(
                       primary = Color(0xFFYourPrimaryColor),
                       onPrimary = Color(0xFFYourOnPrimaryColor),
                       primaryContainer = Color(0xFFYourPrimaryContainerColor),
                       // ... 其他颜色
                   )
               } else {
                   // 亮色模式配色
                   lightColorScheme(
                       primary = Color(0xFFYourPrimaryColor),
                       onPrimary = Color(0xFFYourOnPrimaryColor),
                       primaryContainer = Color(0xFFYourPrimaryContainerColor),
                       // ... 其他颜色
                   )
               }
           }
           // 其他主题
       }
   }
   ```

3. **添加主题资源**：在`strings.xml`中添加主题名称
   ```xml
   <string name="theme_new_theme">新主题名称</string>
   ```

4. **更新设置界面**：在`SettingsScreen.kt`中添加新主题选择项
   ```kotlin
   ThemeColorItem(
       name = stringResource(R.string.theme_new_theme),
       color = Color(0xFFYourPrimaryColor),
       isSelected = uiState.themeType == ThemeType.NEW_THEME,
       onClick = { 
           viewModel.updateThemeType(ThemeType.NEW_THEME)
           AppThemeManager.updateThemeType(ThemeType.NEW_THEME)
       },
       modifier = Modifier.weight(1f)
   )
   ```

5. **测试**：确保在不同屏幕尺寸和深色/亮色模式下主题显示正常

#### 6.1.2 主题颜色指南

Material Design 3建议每个主题至少定义以下颜色：
- **Primary**: 主色，用于主要UI元素
- **onPrimary**: 在主色上的文本/图标颜色
- **PrimaryContainer**: 主色容器，用于次要UI元素
- **onPrimaryContainer**: 在主色容器上的文本/图标颜色
- **Secondary**: 次色，用于次要UI元素
- **Surface**: 表面颜色，用于卡片等UI元素
- **Background**: 背景色

建议使用Material Theme Builder工具帮助生成完整的颜色方案。

### 6.2 新增语言

#### 6.2.1 步骤详解

1. **添加语言常量**：在`LanguageManager.kt`中添加新语言的代码常量
   ```kotlin
   // 语言代码常量
   const val LANGUAGE_FRENCH = "fr" // 法语
   ```

2. **更新语言映射**：在`LanguageManager`中添加语言代码和名称的映射
   ```kotlin
   // 语言显示名称到语言代码的映射
   private val languageNameToCode = mapOf(
       "简体中文" to LANGUAGE_CHINESE_SIMPLIFIED,
       "繁體中文" to LANGUAGE_CHINESE_TRADITIONAL,
       "English" to LANGUAGE_ENGLISH,
       "日本語" to LANGUAGE_JAPANESE,
       "Français" to LANGUAGE_FRENCH // 添加法语
   )
   
   // 语言代码到语言显示名称的映射
   private val languageCodeToName = mapOf(
       LANGUAGE_CHINESE_SIMPLIFIED to "简体中文",
       LANGUAGE_CHINESE_TRADITIONAL to "繁體中文",
       LANGUAGE_ENGLISH to "English",
       LANGUAGE_JAPANESE to "日本語",
       LANGUAGE_FRENCH to "Français" // 添加法语
   )
   ```

3. **更新语言区域逻辑**：在`updateResources`和`LocaleAwareComposable`中添加新语言的Locale创建
   ```kotlin
   val locale = when {
       languageCode.startsWith("zh-CN") -> Locale.SIMPLIFIED_CHINESE
       languageCode.startsWith("zh-TW") -> Locale.TRADITIONAL_CHINESE
       languageCode.startsWith("en") -> Locale.ENGLISH
       languageCode.startsWith("ja") -> Locale.JAPANESE
       languageCode.startsWith("fr") -> Locale.FRENCH // 添加法语
       else -> Locale.getDefault()
   }
   ```

4. **添加语言资源文件**：创建对应语言的资源文件目录和字符串资源
   - 创建`res/values-fr`目录
   - 添加`strings.xml`文件，包含所有翻译
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <resources>
       <string name="app_name">Seeding</string>
       <string name="nav_action">Semer</string>
       <string name="nav_goal">Objectifs</string>
       <string name="nav_harvest">Récolter</string>
       <!-- 其他翻译 -->
   </resources>
   ```

5. **更新设置界面**：在`SettingsScreen.kt`的语言列表中添加新选项
   ```kotlin
   val languages = listOf(
       stringResource(R.string.language_zh_cn),
       stringResource(R.string.language_zh_tw),
       stringResource(R.string.language_en),
       stringResource(R.string.language_ja),
       stringResource(R.string.language_fr) // 添加法语
   )
   ```

6. **添加语言选项字符串**：在所有语言的`strings.xml`中添加新语言的显示名称
   ```xml
   <string name="language_fr">Français</string>
   ```

7. **测试**：确保所有页面在新语言下正确显示

#### 6.2.2 翻译管理建议

- 使用Excel或Google Sheets管理所有语言的翻译，便于对比和更新
- 按功能模块组织字符串，方便定位
- 使用翻译变量确保一致性，如`%1$s`表示插入的名称
- 考虑使用专业翻译服务确保准确性

### 6.3 新增页面

#### 6.3.1 创建新页面的完整流程

1. **定义路由**：在`Screen.kt`中添加新页面路由
   ```kotlin
   sealed class Screen(val route: String) {
       // ... 现有页面
       object NewFeature : Screen("new_feature")
       // 如果有子页面
       object NewFeatureDetail : Screen("new_feature/{itemId}") {
           fun createRoute(itemId: String) = "new_feature/$itemId"
       }
   }
   ```

2. **创建ViewModel**：创建负责页面逻辑的ViewModel类
   ```kotlin
   @HiltViewModel
   class NewFeatureViewModel @Inject constructor(
       private val repository: NewFeatureRepository
   ) : ViewModel() {
       // UI状态
       private val _uiState = MutableStateFlow(NewFeatureUiState())
       val uiState: StateFlow<NewFeatureUiState> = _uiState.asStateFlow()
       
       // 初始化
       init {
           loadData()
       }
       
       // 加载数据
       private fun loadData() {
           viewModelScope.launch {
               try {
                   _uiState.update { it.copy(isLoading = true) }
                   val data = repository.getData()
                   _uiState.update { 
                       it.copy(
                           data = data,
                           isLoading = false
                       ) 
                   }
               } catch (e: Exception) {
                   _uiState.update { 
                       it.copy(
                           errorMessage = e.message ?: "加载失败",
                           isLoading = false
                       ) 
                   }
               }
           }
       }
       
       // 其他操作方法...
   }
   
   // UI状态数据类
   data class NewFeatureUiState(
       val data: List<NewFeatureItem> = emptyList(),
       val isLoading: Boolean = false,
       val errorMessage: String? = null
   )
   ```

3. **创建UI界面**：创建Composable函数实现界面
   ```kotlin
   @Composable
   fun NewFeatureScreen(
       navController: NavController,
       viewModel: NewFeatureViewModel = hiltViewModel()
   ) {
       // 收集UI状态
       val uiState by viewModel.uiState.collectAsState()
       
       // 页面布局
       Column(
           modifier = Modifier
               .fillMaxSize()
               .padding(16.dp)
       ) {
           // 标题
           Text(
               text = stringResource(R.string.new_feature_title),
               style = MaterialTheme.typography.headlineMedium,
               modifier = Modifier.padding(bottom = 16.dp)
           )
           
           // 加载状态
           if (uiState.isLoading) {
               CircularProgressIndicator(
                   modifier = Modifier.align(Alignment.CenterHorizontally)
               )
           }
           
           // 错误消息
           uiState.errorMessage?.let { error ->
               Text(
                   text = error,
                   color = MaterialTheme.colorScheme.error,
                   modifier = Modifier.padding(vertical = 8.dp)
               )
           }
           
           // 内容列表
           LazyColumn {
               items(uiState.data) { item ->
                   NewFeatureItem(
                       item = item,
                       onClick = {
                           // 导航到详情页
                           navController.navigate(
                               Screen.NewFeatureDetail.createRoute(item.id)
                           )
                       }
                   )
               }
           }
           
           // 其他UI元素...
       }
   }
   
   @Composable
   private fun NewFeatureItem(
       item: NewFeatureItem,
       onClick: () -> Unit
   ) {
       // 实现列表项UI
   }
   ```

4. **添加导航配置**：在`SeedingNavigation.kt`中添加新路由
   ```kotlin
   @Composable
   fun SeedingNavigation(
       navController: NavHostController,
       startDestination: String = Screen.Splash.route
   ) {
       NavHost(
           navController = navController,
           startDestination = startDestination
       ) {
           // ... 现有路由
           
           // 新页面路由
           composable(Screen.NewFeature.route) {
               NewFeatureScreen(navController = navController)
           }
           
           // 新页面详情路由
           composable(
               route = Screen.NewFeatureDetail.route,
               arguments = listOf(
                   navArgument("itemId") { type = NavType.StringType }
               )
           ) { backStackEntry ->
               val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
               NewFeatureDetailScreen(
                   itemId = itemId,
                   navController = navController
               )
           }
       }
   }
   ```

5. **添加到导航菜单**：根据需要添加到底部导航栏或抽屉菜单
   - 添加到底部导航栏：
   ```kotlin
   val bottomNavItems = listOf(
       // ... 现有项目
       BottomNavItem(
           route = Screen.NewFeature.route,
           selectedIcon = Icons.Filled.NewIcon,
           unselectedIcon = Icons.Outlined.NewIcon,
           title = stringResource(R.string.nav_new_feature)
       )
   )
   ```
   - 添加到抽屉菜单：
   ```kotlin
   val drawerItems = listOf(
       // ... 现有项目
       DrawerItem(
           route = Screen.NewFeature.route,
           icon = Icons.Default.NewIcon,
           title = stringResource(R.string.nav_new_feature)
       )
   )
   ```

6. **添加资源字符串**：在`strings.xml`中添加相关字符串
   ```xml
   <string name="nav_new_feature">新功能</string>
   <string name="new_feature_title">新功能标题</string>
   <!-- 其他字符串 -->
   ```

7. **创建相关数据模型和仓库**：
   ```kotlin
   // 数据模型
   data class NewFeatureItem(
       val id: String,
       val title: String,
       val description: String,
       // 其他属性
   )
   
   // 仓库接口
   interface NewFeatureRepository {
       suspend fun getData(): List<NewFeatureItem>
       // 其他方法
   }
   
   // 仓库实现
   class NewFeatureRepositoryImpl @Inject constructor(
       private val api: ApiService,
       private val dao: NewFeatureDao
   ) : NewFeatureRepository {
       override suspend fun getData(): List<NewFeatureItem> {
           // 实现逻辑
       }
   }
   ```

8. **更新依赖注入模块**：在Hilt模块中添加新仓库
   ```kotlin
   @Module
   @InstallIn(SingletonComponent::class)
   object RepositoryModule {
       // ... 现有绑定
       
       @Provides
       @Singleton
       fun provideNewFeatureRepository(
           api: ApiService,
           dao: NewFeatureDao
       ): NewFeatureRepository {
           return NewFeatureRepositoryImpl(api, dao)
       }
   }
   ```

#### 6.3.2 页面设计最佳实践

- **状态提升**：将状态尽可能提升到ViewModel，保持UI无状态
- **组件化**：将复杂UI拆分为可重用组件
- **预览支持**：添加`@Preview`注解帮助UI开发
- **错误处理**：实现优雅的错误状态和重试机制
- **加载状态**：添加骨架屏或加载指示器

### 6.4 新增自定义组件

#### 6.4.1 创建可复用组件

1. **定义组件接口**：确定组件参数和事件回调
   ```kotlin
   @Composable
   fun CustomComponent(
       title: String,
       description: String? = null,
       isEnabled: Boolean = true,
       onClick: () -> Unit,
       modifier: Modifier = Modifier
   ) {
       // 实现组件UI
   }
   ```

2. **提供默认样式**：遵循Material Design指南，使用主题颜色
   ```kotlin
   val backgroundColor = when {
       !isEnabled -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
       else -> MaterialTheme.colorScheme.surface
   }
   
   val contentColor = when {
       !isEnabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
       else -> MaterialTheme.colorScheme.onSurface
   }
   ```

3. **提供布局修饰符**：允许自定义样式
   ```kotlin
   Surface(
       modifier = modifier
           .fillMaxWidth()
           .clickable(
               enabled = isEnabled,
               onClick = onClick
           ),
       shape = MaterialTheme.shapes.medium,
       color = backgroundColor,
       contentColor = contentColor
   ) {
       // 组件内容
   }
   ```

4. **添加辅助功能支持**：
   ```kotlin
   .semantics {
       contentDescription = "$title ${description ?: ""}"
       disabled = !isEnabled
   }
   ```

5. **创建预览**：
   ```kotlin
   @Preview(showBackground = true)
   @Composable
   private fun CustomComponentPreview() {
       SeedingTheme {
           CustomComponent(
               title = "示例标题",
               description = "示例描述文本",
               onClick = {}
           )
       }
   }
   ```

#### 6.4.2 组件最佳实践

- **参数默认值**：为非必需参数提供合理默认值
- **修饰符透传**：始终包含`modifier`参数并应用
- **状态提升**：保持组件无状态，通过参数和回调传递状态
- **主题适应**：使用MaterialTheme的颜色和排版
- **内容槽**：对于复杂组件，提供`content`参数允许自定义内容

### 6.5 数据存储和API集成

#### 6.5.1 本地数据存储

1. **SharedPreferences**：适用于简单键值对
   ```kotlin
   // 保存设置
   fun saveSettings(context: Context) {
       val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
       prefs.edit()
           .putString("theme_type", currentThemeType.name)
           .putBoolean("dark_mode", isDarkMode)
           .putFloat("font_size", fontSizeScale)
           .apply()
   }
   
   // 加载设置
   fun loadSettings(context: Context) {
       val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
       val themeTypeName = prefs.getString("theme_type", ThemeType.DEFAULT.name)
       try {
           currentThemeType = ThemeType.valueOf(themeTypeName ?: ThemeType.DEFAULT.name)
       } catch (e: Exception) {
           currentThemeType = ThemeType.DEFAULT
       }
       isDarkMode = prefs.getBoolean("dark_mode", false)
       fontSizeScale = prefs.getFloat("font_size", 1.0f)
   }
   ```

2. **Room数据库**：适用于结构化数据
   - 定义实体：
   ```kotlin
   @Entity(tableName = "seeds")
   data class SeedEntity(
       @PrimaryKey(autoGenerate = true) val id: Int = 0,
       val title: String,
       val description: String,
       val createdDate: Long,
       val targetDate: Long?,
       val status: Int
   )
   ```
   - 创建DAO：
   ```kotlin
   @Dao
   interface SeedDao {
       @Query("SELECT * FROM seeds ORDER BY createdDate DESC")
       fun getAllSeeds(): Flow<List<SeedEntity>>
       
       @Insert
       suspend fun insertSeed(seed: SeedEntity): Long
       
       @Update
       suspend fun updateSeed(seed: SeedEntity)
       
       @Delete
       suspend fun deleteSeed(seed: SeedEntity)
   }
   ```
   - 创建数据库：
   ```kotlin
   @Database(entities = [SeedEntity::class], version = 1)
   abstract class AppDatabase : RoomDatabase() {
       abstract fun seedDao(): SeedDao
   }
   ```
   - 依赖注入配置：
   ```kotlin
   @Module
   @InstallIn(SingletonComponent::class)
   object DatabaseModule {
       @Provides
       @Singleton
       fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
           return Room.databaseBuilder(
               context,
               AppDatabase::class.java,
               "seeding_database"
           ).build()
       }
       
       @Provides
       fun provideSeedDao(database: AppDatabase): SeedDao {
           return database.seedDao()
       }
   }
   ```

#### 6.5.2 API集成

1. **定义API接口**：使用Retrofit
   ```kotlin
   interface ApiService {
       @GET("seeds")
       suspend fun getSeeds(): List<SeedDto>
       
       @POST("seeds")
       suspend fun createSeed(@Body seed: SeedRequest): SeedResponse
       
       @PUT("seeds/{id}")
       suspend fun updateSeed(
           @Path("id") id: String,
           @Body seed: SeedRequest
       ): SeedResponse
       
       @DELETE("seeds/{id}")
       suspend fun deleteSeed(@Path("id") id: String)
   }
   ```

2. **创建API模块**：
   ```kotlin
   @Module
   @InstallIn(SingletonComponent::class)
   object NetworkModule {
       @Provides
       @Singleton
       fun provideOkHttpClient(): OkHttpClient {
           return OkHttpClient.Builder()
               .addInterceptor(HttpLoggingInterceptor().apply {
                   level = HttpLoggingInterceptor.Level.BODY
               })
               .connectTimeout(30, TimeUnit.SECONDS)
               .readTimeout(30, TimeUnit.SECONDS)
               .build()
       }
       
       @Provides
       @Singleton
       fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
           return Retrofit.Builder()
               .baseUrl("https://api.example.com/")
               .client(okHttpClient)
               .addConverterFactory(GsonConverterFactory.create())
               .build()
       }
       
       @Provides
       @Singleton
       fun provideApiService(retrofit: Retrofit): ApiService {
           return retrofit.create(ApiService::class.java)
       }
   }
   ```

3. **实现仓库层**：
   ```kotlin
   class SeedRepositoryImpl @Inject constructor(
       private val api: ApiService,
       private val seedDao: SeedDao
   ) : SeedRepository {
       override fun getSeeds(): Flow<List<Seed>> {
           return seedDao.getAllSeeds()
               .map { entities ->
                   entities.map { it.toDomain() }
               }
       }
       
       override suspend fun refreshSeeds() {
           try {
               val remoteSeeds = api.getSeeds()
               seedDao.insertSeeds(remoteSeeds.map { it.toEntity() })
           } catch (e: Exception) {
               // 处理错误，可能回退到缓存数据
           }
       }
       
       // 其他方法
   }
   ```

### 6.6 调试与测试

#### 6.6.1 调试技巧

1. **Compose调试**：启用Compose调试器
   ```kotlin
   // 在onCreate中添加
   if (BuildConfig.DEBUG) {
       Compose.setViewCompositionStrategy(DebugCompositionStrategy())
   }
   ```

2. **添加日志**：使用自定义日志工具类
   ```kotlin
   object Logger {
       private const val TAG = "SeedingApp"
       
       fun d(message: String) {
           if (BuildConfig.DEBUG) {
               Log.d(TAG, message)
           }
       }
       
       fun e(message: String, throwable: Throwable? = null) {
           Log.e(TAG, message, throwable)
       }
   }
   ```

3. **导航调试**：打印导航状态
   ```kotlin
   val navBackStackEntry by navController.currentBackStackEntryAsState()
   
   LaunchedEffect(navBackStackEntry) {
       Logger.d("导航到: ${navBackStackEntry?.destination?.route}")
   }
   ```

#### 6.6.2 编写测试

1. **ViewModel测试**：
   ```kotlin
   @RunWith(JUnit4::class)
   class SettingsViewModelTest {
       @get:Rule
       val instantTaskExecutorRule = InstantTaskExecutorRule()
       
       private val testDispatcher = TestCoroutineDispatcher()
       
       @ExperimentalCoroutinesApi
       @Before
       fun setup() {
           Dispatchers.setMain(testDispatcher)
       }
       
       @ExperimentalCoroutinesApi
       @After
       fun tearDown() {
           Dispatchers.resetMain()
           testDispatcher.cleanupTestCoroutines()
       }
       
       @Test
       fun `when updateLanguage called, then uiState updated`() = runBlockingTest {
           // Given
           val application = mock(Application::class.java)
           val viewModel = SettingsViewModel(application)
           
           // When
           viewModel.updateLanguage("English")
           
           // Then
           val uiState = viewModel.uiState.value
           assertEquals("English", uiState.language)
       }
   }
   ```

2. **Compose UI测试**：
   ```kotlin
   @RunWith(AndroidJUnit4::class)
   class SettingsScreenTest {
       @get:Rule
       val composeTestRule = createComposeRule()
       
       @Test
       fun settingsScreen_displaysCorrectThemes() {
           // Given
           val navController = TestNavHostController(
               ApplicationProvider.getApplicationContext()
           )
           
           // When
           composeTestRule.setContent {
               SettingsScreen(navController = navController)
           }
           
           // Then
           composeTestRule.onNodeWithText("DEFAULT").assertIsDisplayed()
           composeTestRule.onNodeWithText("NATURE").assertIsDisplayed()
           // 其他断言
       }
   }
   ```

## 7. 常见问题与解决方案

### 7.1 主题或语言切换不生效

- **问题**：更改主题或语言后，UI没有更新
- **原因**：
  1. 未正确更新全局状态
  2. UI组件未订阅状态变化
  3. 资源未正确加载
- **解决方案**：
  1. 确保调用`AppThemeManager.updateThemeType`或`LanguageManager.switchLanguage`
  2. 检查组件是否正确引用`LocalLanguageState.current`或`MaterialTheme`
  3. 显式触发资源重载：`context.resources.updateConfiguration(...)`
  4. 确保在`SettingsViewModel`中同时更新ViewModel状态和全局状态

### 7.2 页面导航问题

- **问题**：导航到新页面后，返回键行为异常
- **原因**：
  1. 导航参数配置错误
  2. `popUpTo`和`inclusive`设置不当
  3. 路由名称不匹配
- **解决方案**：
  1. 检查`NavHost`和`composable`的路由定义
  2. 确保`popUpTo`正确指向目标路由
  3. 使用`launchSingleTop = true`避免页面堆叠
  4. 调试BackStack：
  ```kotlin
  LaunchedEffect(navController) {
      navController.currentBackStackEntryFlow.collect { entry ->
          Logger.d("当前路由: ${entry.destination.route}, 参数: ${entry.arguments}")
      }
  }
  ```

### 7.3 性能优化

- **问题**：滚动列表或复杂UI卡顿
- **原因**：
  1. Composable函数重组过于频繁
  2. 高开销操作在组合阶段执行
  3. 未正确使用`LazyColumn`或`key`
- **解决方案**：
  1. 使用`remember`和`derivedStateOf`减少重组
  ```kotlin
  val sortedItems by remember(items) {
      derivedStateOf { items.sortedBy { it.timestamp } }
  }
  ```
  2. 将高开销操作移至`LaunchedEffect`
  ```kotlin
  LaunchedEffect(key1) {
      // 执行高开销操作
  }
  ```
  3. 为列表项提供稳定的key
  ```kotlin
  LazyColumn {
      items(
          items = uiState.items,
          key = { it.id } // 稳定的唯一标识符
      ) { item ->
          ItemComponent(item)
      }
  }
  ```
  4. 使用`Modifier.composed`创建自定义修饰符
  ```kotlin
  fun Modifier.shimmerEffect(): Modifier = composed {
      // 实现闪烁效果
  }
  ```

## 8. 未来扩展方向

### 8.1 数据持久化完善
- 实现Room数据库全面集成
- 添加数据同步机制与云端
- 实现离线优先策略

### 8.2 高级主题系统
- 支持用户自定义颜色
- 实现动态主题（基于时间或位置）
- 添加Material You动态颜色支持

### 8.3 动画与交互
- 添加页面转场动画
- 实现手势操作
- 添加微交互反馈

### 8.4 可访问性增强
- 改进屏幕阅读器支持
- 添加高对比度模式
- 支持动态字体大小

### 8.5 性能监控
- 集成性能监控工具
- 优化启动时间
- 减少内存使用

---

本文档详细介绍了Seeding应用的开发指南，包括主题和语言系统实现、组件开发、页面导航以及常见问题解决方案。开发者可参考本文档进行功能扩展和维护。如有问题，请联系项目维护者。 