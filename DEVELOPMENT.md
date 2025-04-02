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

### 5.4 Room数据库集成

Room数据库集成通过以下步骤实现：

1. **实体定义**：在`data/local/entity`包中定义数据库实体类
2. **DAO接口**：在`data/local/dao`包中定义数据访问对象接口
3. **类型转换器**：在`Converters`类中实现复杂类型转换
4. **数据库类**：创建`SeedingDatabase`类统一管理数据库
5. **依赖注入**：通过Hilt注入数据库和DAO组件
6. **仓库实现**：使用Room DAO实现仓库接口

数据初始化通过`SeedingApplication`中的初始化代码完成：
```kotlin
private fun initializeData() {
    applicationScope.launch(Dispatchers.IO) {
        try {
            // 初始化种子数据
            seedRepository.initializeSeeds()
            Log.d("SeedingApplication", "应用数据初始化完成")
        } catch (e: Exception) {
            Log.e("SeedingApplication", "初始化数据失败: ${e.message}", e)
        }
    }
}
```

数据库创建时的回调还会自动填充初始数据：
```kotlin
.addCallback(object : Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // 在数据库创建时预填充种子数据
        INSTANCE?.let { database ->
            CoroutineScope(Dispatchers.IO).launch {
                val seedDao = database.seedDao()
                val seedEntities = SeedUtils.getAllSeedModels().map { seed ->
                    SeedEntity(/* ... */)
                }
                seedDao.insertSeeds(seedEntities)
            }
        }
    }
})
```

### 5.5 实体-模型映射

应用采用了多层架构，不同层使用不同的数据模型：
1. **实体(Entity)**：数据库层使用，包含持久化相关字段
2. **领域模型(Model)**：业务逻辑层使用，包含业务相关字段
3. **UI模型(UiState)**：UI层使用，包含显示相关字段

`EntityMappers`类负责在这些模型之间进行转换：
```kotlin
object EntityMappers {
    // 实体到领域模型的转换
    fun mapToGoal(entity: GoalEntity): Goal { /* ... */ }
    
    // 领域模型到实体的转换
    fun mapToGoalEntity(model: Goal): GoalEntity { /* ... */ }
    
    // 其他转换方法...
}
```

这种分层架构确保了关注点分离，使代码更易于测试和维护。

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

### 6.2 新增数据库实体与DAO

要添加新的数据库实体和相应的数据访问对象，请按照以下步骤操作：

1. **创建实体类**：在`data/local/entity`包中创建新的实体类
   ```kotlin
   @Entity(tableName = "your_entity_table")
   data class YourEntity(
       @PrimaryKey(autoGenerate = true)
       val id: Long = 0,
       val name: String,
       val description: String,
       // 其他字段...
       
       // 可选的关系字段
       @ForeignKey(
           entity = ParentEntity::class,
           parentColumns = ["id"],
           childColumns = ["parentId"],
           onDelete = ForeignKey.CASCADE
       )
       val parentId: String
   )
   ```

2. **创建DAO接口**：在`data/local/dao`包中创建新的DAO接口
   ```kotlin
   @Dao
   interface YourEntityDao {
       @Insert(onConflict = OnConflictStrategy.REPLACE)
       suspend fun insert(entity: YourEntity): Long
       
       @Update
       suspend fun update(entity: YourEntity)
       
       @Delete
       suspend fun delete(entity: YourEntity)
       
       @Query("SELECT * FROM your_entity_table WHERE id = :id")
       suspend fun getById(id: Long): YourEntity?
       
       @Query("SELECT * FROM your_entity_table ORDER BY name ASC")
       fun getAll(): Flow<List<YourEntity>>
       
       // 其他查询方法...
   }
   ```

3. **更新数据库类**：在`SeedingDatabase`类中添加新实体和DAO
   ```kotlin
   @Database(
       entities = [
           UserEntity::class,
           SeedEntity::class,
           GoalEntity::class,
           YourEntity::class // 添加新实体
       ],
       version = 1,
       exportSchema = false
   )
   abstract class SeedingDatabase : RoomDatabase() {
       abstract fun userDao(): UserDao
       abstract fun seedDao(): SeedDao
       abstract fun goalDao(): GoalDao
       abstract fun yourEntityDao(): YourEntityDao // 添加新DAO
       
       // 其他代码...
   }
   ```

4. **添加依赖注入**：在`DatabaseModule`中添加新DAO的提供方法
   ```kotlin
   @Provides
   fun provideYourEntityDao(database: SeedingDatabase): YourEntityDao {
       return database.yourEntityDao()
   }
   ```

5. **创建领域模型和映射器**：创建对应的领域模型和映射方法
   ```kotlin
   // 领域模型
   data class YourModel(
       val id: Long,
       val name: String,
       val description: String,
       // 其他字段...
   )
   
   // 在EntityMappers中添加映射方法
   fun mapToYourModel(entity: YourEntity): YourModel {
       return YourModel(
           id = entity.id,
           name = entity.name,
           description = entity.description,
           // 其他字段映射...
       )
   }
   
   fun mapToYourEntity(model: YourModel): YourEntity {
       return YourEntity(
           id = model.id,
           name = model.name,
           description = model.description,
           // 其他字段映射...
       )
   }
   ```

6. **创建仓库接口和实现**：创建仓库接口和实现类
   ```kotlin
   // 仓库接口
   interface YourRepository {
       fun getAll(): Flow<List<YourModel>>
       suspend fun getById(id: Long): YourModel?
       suspend fun save(model: YourModel): Long
       suspend fun update(model: YourModel)
       suspend fun delete(model: YourModel)
   }
   
   // 仓库实现
   @Singleton
   class YourRepositoryImpl @Inject constructor(
       private val yourEntityDao: YourEntityDao
   ) : YourRepository {
       // 实现仓库接口方法...
   }
   ```

7. **更新仓库模块**：在`RepositoryModule`中添加新仓库的绑定
   ```kotlin
   @Binds
   @Singleton
   abstract fun bindYourRepository(impl: YourRepositoryImpl): YourRepository
   ```

### 6.3 后端与云端集成准备

当前的架构设计中已经为未来的云端集成做了准备：

1. **实体同步标记**：`GoalEntity`中包含`isSynced`字段，用于标记是否已与云端同步
2. **未同步数据查询**：`GoalDao`中包含获取未同步数据的方法
   ```kotlin
   @Query("SELECT * FROM goals WHERE userId = :userId AND isSynced = 0")
   suspend fun getUnsyncedGoals(userId: String): List<GoalEntity>
   ```
3. **数据同步状态更新**：`GoalDao`中包含更新同步状态的方法
   ```kotlin
   @Query("UPDATE goals SET isSynced = :synced WHERE goalId = :goalId")
   suspend fun updateSyncStatus(goalId: String, synced: Boolean)
   ```

要实现完整的云端集成，需要进一步完成以下步骤：

1. **API服务定义**：在`data/remote`包中定义API接口
   ```kotlin
   interface GoalApiService {
       @GET("goals")
       suspend fun getGoals(@Query("userId") userId: String): List<GoalDto>
       
       @POST("goals")
       suspend fun createGoal(@Body goal: GoalDto): GoalDto
       
       @PUT("goals/{goalId}")
       suspend fun updateGoal(@Path("goalId") goalId: String, @Body goal: GoalDto): GoalDto
       
       @DELETE("goals/{goalId}")
       suspend fun deleteGoal(@Path("goalId") goalId: String)
   }
   ```

2. **数据传输对象**：创建DTO类进行网络通信
   ```kotlin
   data class GoalDto(
       val id: String,
       val userId: String,
       val title: String,
       val description: String,
       val seedIds: List<Int>,
       val deadline: Long,
       val createdAt: Long,
       val status: String,
       // 其他字段...
   )
   ```

3. **同步服务**：创建同步服务处理数据同步
   ```kotlin
   @Singleton
   class SyncService @Inject constructor(
       private val goalRepository: GoalRepository,
       private val goalApiService: GoalApiService,
       private val goalDao: GoalDao
   ) {
       suspend fun syncGoals(userId: String) {
           try {
               // 获取未同步的本地数据
               val unsyncedGoals = goalDao.getUnsyncedGoals(userId)
               
               // 上传未同步数据
               for (goal in unsyncedGoals) {
                   // 转换为DTO
                   val goalDto = goal.toDto()
                   
                   // 根据操作类型执行不同API调用
                   when (/* 根据状态判断操作类型 */) {
                       // 创建
                       // 更新
                       // 删除
                   }
                   
                   // 更新同步状态
                   goalDao.updateSyncStatus(goal.goalId, true)
               }
               
               // 获取云端数据并更新本地
               val remoteGoals = goalApiService.getGoals(userId)
               // 更新本地数据库
           } catch (e: Exception) {
               // 处理同步错误
               Log.e("SyncService", "同步失败: ${e.message}", e)
           }
       }
   }
   ```

4. **网络状态监听**：添加网络状态监听以自动触发同步
   ```kotlin
   @Singleton
   class NetworkMonitor @Inject constructor(
       @ApplicationContext private val context: Context
   ) {
       private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
       
       fun isNetworkAvailable(): Boolean {
           val network = connectivityManager.activeNetwork ?: return false
           val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
           return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
       }
       
       fun observeNetworkState(): Flow<Boolean> {
           return callbackFlow {
               // 实现网络状态监听
           }
       }
   }
   ```

5. **工作管理器集成**：使用WorkManager安排定期同步
   ```kotlin
   @HiltWorker
   class SyncWorker @AssistedInject constructor(
       @Assisted context: Context,
       @Assisted params: WorkerParameters,
       private val syncService: SyncService,
       private val userRepository: UserRepository
   ) : CoroutineWorker(context, params) {
       override suspend fun doWork(): Result {
           return try {
               val currentUser = userRepository.getCurrentUser().first()
               if (currentUser != null) {
                   syncService.syncGoals(currentUser.id)
                   Result.success()
               } else {
                   Result.failure()
               }
           } catch (e: Exception) {
               Log.e("SyncWorker", "同步失败: ${e.message}", e)
               Result.retry()
           }
       }
   }
   ```

## 8. 未来扩展方向

### 8.1 云端同步与离线功能

- **实现API服务**：创建RESTful API或GraphQL服务
- **同步策略**：实现增量同步和冲突解决
- **离线模式**：支持无网络环境下的正常使用
- **批量同步**：批量处理积累的未同步数据
- **同步历史**：提供数据同步历史记录
- **定时同步**：使用WorkManager实现定期后台同步

### 8.2 多用户支持与用户认证

- **用户注册和登录**：实现完整的用户认证流程
- **OAuth集成**：支持第三方账号登录
- **会话管理**：实现安全的用户会话管理
- **权限系统**：实现基于角色的权限控制
- **多设备同步**：支持用户在多设备间同步数据

### 8.3 高级数据分析与统计

- **数据可视化**：实现用户行为和目标完成情况的图表展示
- **趋势分析**：展示用户习惯和行为的变化趋势
- **成就系统**：根据用户行为和目标完成情况颁发成就
- **个性化建议**：基于历史数据提供个性化建议
- **导出功能**：支持数据导出为多种格式

### 8.4 性能优化与安全增强

- **数据库性能**：实现更高效的查询和索引
- **数据加密**：对敏感数据进行加密存储
- **二进制大对象优化**：优化大文件的存储和传输
- **批量操作**：支持高效的批量数据操作
- **安全日志**：实现详细的安全审计日志

### 8.5 扩展社交功能

- **好友系统**：实现用户之间的好友关系
- **分享功能**：支持目标和成就的社交分享
- **协作目标**：支持多人参与的协作目标
- **评论系统**：允许好友对目标进行评论和鼓励
- **社区功能**：建立用户兴趣社区

---

本文档详细介绍了Seeding应用的开发指南，包括主题和语言系统实现、数据库和后端架构，以及未来扩展方向。开发者可参考本文档进行功能扩展和维护。如有问题，请联系项目维护者。 