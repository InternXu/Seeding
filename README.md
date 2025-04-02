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
- **日志**：Timber
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
│   ├── model/                 # 领域模型
│   │   ├── Action.kt          # 行为模型
│   │   ├── Commitment.kt      # 承诺模型
│   │   ├── CommitmentStatus.kt # 承诺状态枚举
│   │   ├── CommitmentTimeFrames.kt # 承诺时间框架
│   │   ├── ActionType.kt      # 行为类型枚举
│   │   └── ...                # 其他模型
│   └── repository/            # 仓库实现
├── di/                        # 依赖注入
│   ├── AppModule.kt           # 应用级别模块
│   ├── DatabaseModule.kt      # 数据库模块
│   └── RepositoryModule.kt    # 仓库模块
├── ui/                        # 界面层
│   ├── components/            # 可复用组件
│   ├── navigation/            # 导航组件
│   │   └── SeedingNavigation.kt # 导航配置
│   ├── screens/               # 应用页面
│   │   ├── action/            # 播种页面
│   │   ├── goal/              # 目标页面
│   │   ├── harvest/           # 收获页面
│   │   ├── login/             # 登录页面
│   │   ├── profile/           # 个人信息页面
│   │   └── settings/          # 设置页面
│   ├── theme/                 # 主题定义
│   ├── SearchEvents.kt        # 全局搜索事件
│   └── SeedingApp.kt          # 应用组件
├── util/                      # 工具类
│   ├── DateUtils.kt           # 日期工具
│   ├── LanguageManager.kt     # 多语言管理
│   └── SeedUtils.kt           # 种子工具类
├── SeedingActivity.kt         # 主活动
└── SeedingApplication.kt      # 应用类
```

#### 架构设计

应用采用了Clean Architecture的分层设计，结合MVVM模式实现：

1. **表现层(UI)**：
   - Compose UI组件
   - ViewModel处理UI逻辑和状态
   - 事件驱动型界面状态管理

2. **领域层(Domain)**：
   - 业务逻辑和规则
   - 数据模型
   - 仓库接口

3. **数据层(Data)**：
   - 仓库接口的实现
   - 数据源(本地数据库)
   - 数据映射器

#### 数据模型设计

应用主要模型包括：

1. **行为(Action)**：
   ```kotlin
   data class Action(
       val actionId: String,
       val userId: String,
       val content: String,
       val timestamp: Long,
       val type: ActionType,
       val seedIds: List<Int>,
       val goalIds: List<String> = emptyList(),
       val hasCommitment: Boolean = false,
       var tag: Any? = null
   )
   ```

2. **承诺(Commitment)**：
   ```kotlin
   data class Commitment(
       val commitmentId: String,
       val actionId: String,
       val userId: String,
       val content: String,
       val seedIds: List<Int>,
       val timestamp: Long,
       val deadline: Long,
       val timeFrame: Int,
       val status: CommitmentStatus,
       val completedAt: Long? = null
   )
   ```

3. **种子(Seed)**：
   ```kotlin
   data class Seed(
       val seedId: Int,
       val name: String,
       val description: String,
       val category: String,
       val iconResName: String
   )
   ```

4. **目标(Goal)**：
   ```kotlin
   data class Goal(
       val goalId: String,
       val userId: String,
       val title: String,
       val description: String,
       val seedIds: List<Int>,
       val deadline: Long?,
       val createdAt: Long,
       val status: GoalStatus
   )
   ```

#### 导航系统

应用使用Jetpack Navigation Compose实现页面导航和动画过渡：

```kotlin
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
    // 设置子页面和详情页面...
}

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
            // 定义页面转场动画...
        ) {
            LoginScreen(navController = navController)
        }
        // 其他页面路由...
    }
}
```

#### 页面结构

应用主要包含以下几个页面：

1. **播种页(ActionScreen)**：
   - 展示所有行为记录
   - 承诺管理和跟踪
   - 按时间分组的行为列表
   - 搜索功能

2. **目标页(GoalScreen)**：
   - 展示用户当前的目标列表
   - 支持查看、编辑、删除目标

3. **收获页(HarvestScreen)**：
   - 展示已完成的目标和相关统计
   - 可视化展示用户进度

4. **设置页(SettingsScreen)**：
   - 主题切换
   - 语言切换(中英文支持)
   - 字体大小调整

### 特色功能

#### 承诺系统

Seeding应用的核心功能是承诺系统，帮助用户培养良好习惯：

1. **承诺创建**：
   - 用户添加负面行为后触发承诺创建
   - 设置承诺内容和时间框架(3分钟到3小时)
   - 将该承诺标记为待完成状态

2. **承诺跟踪**：
   - 实时倒计时显示剩余时间
   - 进度条指示剩余百分比
   - 承诺卡片置顶显示提醒用户

3. **承诺状态管理**：
   - PENDING：待完成的承诺
   - FULFILLED：已履行的承诺
   - EXPIRED：已过期的承诺
   - UNFULFILLED：未履行的承诺

4. **承诺完成机制**：
   - 用户可在12小时宽限期内完成承诺
   - 即使状态为EXPIRED，仍可以标记为完成
   - 自动更新状态和统计数据

#### 多语言与主题

应用支持多语言和多主题，通过以下机制实现：

1. **多语言支持**：
   - 使用资源文件存储不同语言字符串
   - LocalLanguageState实现即时语言切换
   - 当前支持中文、英文

2. **主题支持**：
   - 多种预设主题颜色
   - 深色/浅色模式自适应
   - 字体大小可调

3. **动态UI适配**：
   - ProvideLanguageSettings提供语言上下文
   - AppThemeManager管理全局主题状态
   - 响应式UI自动适应主题和语言变化

### 动画与交互

应用通过精心设计的动画和交互提升用户体验：

1. **页面转场动画**：
   - 主页面之间的水平滑动动画
   - 抽屉菜单的展开和收缩动画
   - 登录/注册页面的淡入淡出效果

2. **组件动画**：
   - 进度条的平滑动画
   - 状态变化的淡入淡出
   - 卡片展开/折叠效果

3. **全局导航**：
   - 底部导航栏实现主页面切换
   - 抽屉菜单提供辅助功能入口
   - 自适应返回按钮行为

### 注意事项

1. **依赖版本**：项目使用最新稳定版本的Jetpack库，确保Gradle依赖最新。

2. **Compose UI**：使用Compose UI构建，确保使用最新的Material3设计。

3. **数据持久化**：当前使用Room实现本地存储，未来将支持云端同步。

4. **搜索实现**：搜索功能通过SharedFlow实现全局事件通信。

---

欢迎贡献代码和提出建议！