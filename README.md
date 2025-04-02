# Seeding应用项目说明文档

## Seeding - 播种收获，培养好习惯

Seeding是一款基于Android平台的习惯培养应用，帮助用户将良好习惯"种子"播种、培养并收获。

### 项目架构

#### 技术栈

- **语言**：Kotlin
- **UI框架**：Jetpack Compose
- **架构模式**：MVVM (Model-View-ViewModel)
- **依赖注入**：Hilt
- **数据持久化**：Room
- **异步处理**：Kotlin Coroutines & Flow
- **导航**：Jetpack Navigation Compose
- **Splash Screen API**：应用启动画面

#### 项目结构

```
app/src/main/java/com/example/seeding/
├── data/                      # 数据层
│   ├── local/                 # 本地数据源
│   │   ├── dao/               # 数据访问对象
│   │   ├── entity/            # 数据库实体
│   │   ├── SeedingDatabase.kt # 数据库定义
│   │   └── Converters.kt      # 类型转换器
│   └── repository/            # 仓库实现
├── di/                        # 依赖注入
│   ├── AppModule.kt           # 应用级别模块
│   ├── DatabaseModule.kt      # 数据库模块
│   └── RepositoryModule.kt    # 仓库模块
├── domain/                    # 领域层
│   ├── model/                 # 领域模型
│   └── repository/            # 仓库接口
├── ui/                        # 界面层
│   ├── components/            # 可复用组件
│   ├── screens/               # 应用页面
│   ├── theme/                 # 主题定义
│   └── viewmodels/            # 视图模型
├── util/                      # 工具类
├── SeedingActivity.kt         # 主活动
└── SeedingApplication.kt      # 应用类
```

#### 架构设计

应用采用了Clean Architecture的分层设计，结合MVVM模式实现：

1. **表现层(UI)**：
   - Compose UI组件
   - ViewModel处理UI逻辑和状态

2. **领域层(Domain)**：
   - 业务逻辑和规则
   - 仓库接口定义
   - 领域模型

3. **数据层(Data)**：
   - 仓库接口的实现
   - 数据源(本地数据库)
   - 数据映射器

#### 数据库设计

应用使用Room实现本地数据库，主要实体包括：

1. **用户(User)**：
   ```kotlin
   @Entity(tableName = "users")
   data class UserEntity(
       @PrimaryKey val userId: String,
       val username: String,
       val email: String,
       val avatarUrl: String?,
       val createdAt: Long
   )
   ```

2. **种子(Seed)**：
   ```kotlin
   @Entity(tableName = "seeds")
   data class SeedEntity(
       @PrimaryKey val seedId: Int,
       val name: String,
       val description: String,
       val category: String,
       val iconResName: String
   )
   ```

3. **目标(Goal)**：
   ```kotlin
   @Entity(tableName = "goals")
   data class GoalEntity(
       @PrimaryKey val goalId: String,
       val userId: String,
       val title: String,
       val description: String,
       val seedIds: List<Int>,
       val deadline: Long?,
       val createdAt: Long,
       val status: GoalStatus,
       val isSynced: Boolean = false
   )
   ```

#### 数据访问层

每个实体都有对应的DAO接口，定义了数据操作方法：

1. **UserDao**：
   ```kotlin
   @Dao
   interface UserDao {
       @Query("SELECT * FROM users WHERE userId = :userId")
       suspend fun getUserById(userId: String): UserEntity?
       
       @Insert(onConflict = OnConflictStrategy.REPLACE)
       suspend fun insertUser(user: UserEntity)
       
       // 其他查询方法...
   }
   ```

2. **SeedDao**：
   ```kotlin
   @Dao
   interface SeedDao {
       @Query("SELECT * FROM seeds")
       fun getAllSeeds(): Flow<List<SeedEntity>>
       
       @Query("SELECT * FROM seeds WHERE seedId IN (:seedIds)")
       suspend fun getSeedsByIds(seedIds: List<Int>): List<SeedEntity>
       
       @Insert(onConflict = OnConflictStrategy.REPLACE)
       suspend fun insertSeeds(seeds: List<SeedEntity>)
       
       // 其他查询方法...
   }
   ```

3. **GoalDao**：
   ```kotlin
   @Dao
   interface GoalDao {
       @Query("SELECT * FROM goals WHERE userId = :userId")
       fun getGoalsByUserId(userId: String): Flow<List<GoalEntity>>
       
       @Insert(onConflict = OnConflictStrategy.REPLACE)
       suspend fun insertGoal(goal: GoalEntity): Long
       
       @Update
       suspend fun updateGoal(goal: GoalEntity)
       
       @Delete
       suspend fun deleteGoal(goal: GoalEntity)
       
       // 其他查询方法...
   }
   ```

#### 仓库层

仓库层封装数据操作逻辑，为业务层提供简洁接口：

1. **UserRepository**：
   ```kotlin
   interface UserRepository {
       fun getCurrentUser(): Flow<User?>
       suspend fun saveUser(user: User)
       suspend fun getUserById(userId: String): User?
   }
   ```

2. **SeedRepository**：
   ```kotlin
   interface SeedRepository {
       fun getAllSeeds(): Flow<List<Seed>>
       suspend fun getSeedsByIds(seedIds: List<Int>): List<Seed>
       suspend fun initializeSeeds()
   }
   ```

3. **GoalRepository**：
   ```kotlin
   interface GoalRepository {
       fun getGoalsByUserId(userId: String): Flow<List<Goal>>
       suspend fun getGoalById(goalId: String): Goal?
       suspend fun saveGoal(goal: Goal): String
       suspend fun updateGoal(goal: Goal)
       suspend fun deleteGoal(goal: Goal)
   }
   ```

### 功能模块

#### 页面结构

应用主要包含以下几个页面：

1. **播种页(SeedingScreen)**：
   - 展示所有可用的种子(好习惯)
   - 用户可以选择种子创建新目标

2. **目标页(GoalScreen)**：
   - 展示用户当前的目标列表
   - 支持查看、编辑、删除目标

3. **收获页(HarvestScreen)**：
   - 展示已完成的目标和相关统计
   - 可视化展示用户进度

4. **设置页(SettingsScreen)**：
   - 主题切换(默认、自然、深夜、高对比度)
   - 语言切换(中英日等多语言支持)
   - 其他应用设置

#### 导航系统

应用使用Jetpack Navigation Compose实现页面导航：

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
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        // 其他页面路由...
    }
}
```

#### UI组件体系

1. **顶部栏(SeedingTopAppBar)**：
   - 页面标题
   - 返回按钮(根据页面)
   - 菜单操作

2. **底部导航栏(SeedingBottomBar)**：
   - 四个主页面的导航按钮
   - 支持选中状态指示

3. **目标卡片(GoalCard)**：
   - 展示目标基本信息
   - 进度指示器
   - 操作按钮

4. **种子项(SeedItem)**：
   - 种子图标
   - 种子名称和描述
   - 选择状态

### 工作流

1. **用户登录/注册**：
   - 当前版本使用本地用户
   - 未来将支持云端账户

2. **创建目标**：
   - 浏览种子列表
   - 选择一个或多个种子
   - 设置目标标题、描述和期限
   - 保存目标

3. **目标管理**：
   - 查看目标列表
   - 更新目标进度
   - 编辑或删除目标

4. **查看收获**：
   - 浏览已完成的目标
   - 查看统计数据和成就
   - 分享成果(计划中)

### 多语言与主题

应用支持多语言和多主题，通过以下机制实现：

1. **多语言支持**：
   - 使用资源文件系统
   - 即时语言切换
   - 当前支持中文、英文、日文

2. **主题支持**：
   - 精心设计的颜色系统
   - 深色模式适配
   - 4种预设主题

### 注意事项

1. **依赖版本**：项目使用最新稳定版本的Jetpack库，确保Gradle依赖最新。

2. **Compose UI**：使用Compose UI构建，确保使用最新的Material3设计。

3. **数据持久化**：当前使用Room实现本地存储，未来将支持云端同步。

---

欢迎贡献代码和提出建议！