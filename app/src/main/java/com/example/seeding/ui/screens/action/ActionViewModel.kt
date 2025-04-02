package com.example.seeding.ui.screens.action

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seeding.data.model.Action
import com.example.seeding.data.model.ActionType
import com.example.seeding.data.model.Commitment
import com.example.seeding.data.model.CommitmentStatus
import com.example.seeding.data.model.CommitmentTimeFrames
import com.example.seeding.data.model.Goal
import com.example.seeding.domain.model.User
import com.example.seeding.domain.repository.ActionRepository
import com.example.seeding.domain.repository.CommitmentRepository
import com.example.seeding.domain.repository.GoalRepository
import com.example.seeding.domain.repository.UserRepository
import com.example.seeding.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ActionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val actionRepository: ActionRepository,
    private val commitmentRepository: CommitmentRepository,
    private val goalRepository: GoalRepository
) : ViewModel() {

    // 当前用户ID
    private val _currentUserId = MutableStateFlow<String?>(null)
    
    // UI状态
    private val _uiState = MutableStateFlow(ActionUiState())
    val uiState: StateFlow<ActionUiState> = _uiState.asStateFlow()
    
    // 所有行为
    private val _allActions = MutableStateFlow<List<Action>>(emptyList())
    
    // The active commitments with PENDING status
    private val _activeCommitments = MutableStateFlow<List<Commitment>>(emptyList())
    
    // All commitments regardless of status
    private val _allCommitments = MutableStateFlow<List<Commitment>>(emptyList())
    
    // 合并状态流提供UI所需数据
    val combinedData = combine(_activeCommitments, _allActions, _uiState, _allCommitments) { commitments, actions, state, allCommitments ->
        ActionScreenData(
            pendingCommitments = commitments,
            actions = actions,
            showDialog = state.showActionDialog,
            dialogMode = state.actionDialogMode,
            currentAction = state.currentAction,
            errorMessage = state.errorMessage,
            allCommitments = allCommitments
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ActionScreenData()
    )
    
    init {
        Log.d(TAG, "初始化ActionViewModel")
        
        viewModelScope.launch {
            try {
                // 尝试获取当前登录用户
                val currentUser = userRepository.getCurrentUser().first()
                
                if (currentUser != null) {
                    Log.d(TAG, "获取到当前用户: ${currentUser.id}")
                    _currentUserId.value = currentUser.id
                    loadData(currentUser.id)
                } else {
                    // 如果没有当前用户，创建默认用户
                    val defaultUser = User(
                        id = "user_1",
                        username = "测试用户",
                        email = "test@example.com",
                        createdAt = System.currentTimeMillis(),
                        lastLoginAt = System.currentTimeMillis()
                    )
                    
                    try {
                        userRepository.saveUser(defaultUser)
                        userRepository.setCurrentUser(defaultUser.id)
                        _currentUserId.value = defaultUser.id
                        loadData(defaultUser.id)
                        Log.d(TAG, "创建默认用户: ${defaultUser.id}")
                    } catch (e: Exception) {
                        Log.e(TAG, "创建默认用户失败: ${e.message}", e)
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "创建用户失败: ${e.localizedMessage}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "初始化用户数据失败: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "初始化数据失败: ${e.localizedMessage}"
                )
            }
        }
    }
    
    // 加载所有数据
    private fun loadData(userId: String) {
        loadActions(userId)
        loadCommitments(userId)
        loadAvailableGoals(userId)
        checkExpiredCommitments(userId)
    }
    
    // 加载行为数据
    private fun loadActions(userId: String) {
        viewModelScope.launch {
            try {
                actionRepository.getActionsByUserId(userId).collect { actions ->
                    _allActions.value = actions
                }
            } catch (e: Exception) {
                Log.e(TAG, "加载行为数据失败: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "加载行为数据失败: ${e.localizedMessage}"
                )
            }
        }
    }
    
    // 加载承诺数据
    private fun loadCommitments(userId: String) {
        viewModelScope.launch {
            try {
                // 加载活跃的承诺（包括待履行和逾期未超过12小时的）
                commitmentRepository.getAllActiveCommitments(userId).collect { commitments ->
                    _activeCommitments.value = commitments
                }
            } catch (e: Exception) {
                Log.e(TAG, "加载承诺数据失败: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "加载承诺数据失败: ${e.localizedMessage}"
                )
            }
        }
        
        // 加载所有承诺，包括已完成和过期的
        viewModelScope.launch {
            try {
                commitmentRepository.getAllCommitmentsByUserId(userId).collect { allCommitments ->
                    _allCommitments.value = allCommitments
                }
            } catch (e: Exception) {
                Log.e(TAG, "加载所有承诺数据失败: ${e.message}", e)
                // 不影响UI显示，仅记录日志
            }
        }
    }
    
    // 加载当前进行中的目标（可选的目标）
    private fun loadAvailableGoals(userId: String) {
        viewModelScope.launch {
            try {
                // 修改为获取所有目标，而不只是活跃的目标
                goalRepository.getAllUserGoals(userId).collect { goals ->
                    _uiState.value = _uiState.value.copy(
                        availableGoals = goals
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "加载目标数据失败: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "加载目标数据失败: ${e.localizedMessage}"
                )
            }
        }
    }
    
    // 检查过期承诺
    private fun checkExpiredCommitments(userId: String) {
        viewModelScope.launch {
            try {
                commitmentRepository.checkAndUpdateExpiredCommitments(userId)
            } catch (e: Exception) {
                Log.e(TAG, "检查过期承诺失败: ${e.message}", e)
            }
        }
    }
    
    // 显示添加行为对话框
    fun showAddActionDialog() {
        _uiState.value = _uiState.value.copy(
            showActionDialog = true,
            actionDialogMode = ActionDialogMode.ADD,
            currentAction = null
        )
    }
    
    // 显示行为详情
    fun showActionDetail(action: Action) {
        _uiState.value = _uiState.value.copy(
            showActionDialog = true,
            actionDialogMode = ActionDialogMode.VIEW,
            currentAction = action
        )
    }
    
    // 隐藏行为对话框
    fun hideActionDialog() {
        _uiState.value = _uiState.value.copy(
            showActionDialog = false,
            currentAction = null
        )
    }
    
    // 添加行为
    fun addAction(
        content: String,
        type: ActionType,
        seedIds: List<Int>,
        goalIds: List<String>
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val userId = _currentUserId.value ?: return@launch
                
                val newAction = Action(
                    actionId = UUID.randomUUID().toString(),
                    userId = userId,
                    content = content,
                    type = type,
                    timestamp = System.currentTimeMillis(),
                    seedIds = seedIds,
                    goalIds = goalIds,
                    hasCommitment = type == ActionType.NEGATIVE
                )
                
                actionRepository.addAction(newAction)
                
                // 无论什么类型的行为，都立即隐藏对话框
                hideActionDialog()
                
                // 如果是负面行为，保存ID用于显示承诺对话框
                if (type == ActionType.NEGATIVE) {
                    _uiState.value = _uiState.value.copy(
                        lastCreatedActionId = newAction.actionId
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "添加行为失败")
                _uiState.value = _uiState.value.copy(
                    errorMessage = "添加行为失败: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    // 添加承诺
    fun addCommitment(
        actionId: String,
        content: String,
        seedIds: List<Int>,
        timeFrame: Int
    ) {
        viewModelScope.launch {
            try {
                val userId = _currentUserId.value ?: return@launch
                
                // 计算截止时间 - 使用分钟而不是天数
                val currentTime = System.currentTimeMillis()
                val deadline = CommitmentTimeFrames.calculateDeadline(timeFrame)
                
                val commitment = Commitment(
                    commitmentId = UUID.randomUUID().toString(),
                    actionId = actionId,
                    userId = userId,
                    content = content,
                    timestamp = currentTime,
                    deadline = deadline,
                    seedIds = seedIds,
                    timeFrame = timeFrame,
                    status = CommitmentStatus.PENDING
                )
                
                commitmentRepository.addCommitment(commitment)
                
                // 更新行为的hasCommitment属性
                actionRepository.getActionById(actionId)?.let { action ->
                    if (!action.hasCommitment) {
                        actionRepository.updateActionHasCommitment(actionId, true)
                    }
                }
                
                // 重置最后创建的行为ID并隐藏对话框
                _uiState.value = _uiState.value.copy(
                    lastCreatedActionId = null
                )
                hideActionDialog()
            } catch (e: Exception) {
                Timber.e(e, "添加承诺失败")
                _uiState.value = _uiState.value.copy(
                    errorMessage = "添加承诺失败: ${e.message}"
                )
                hideActionDialog()
            }
        }
    }
    
    // 完成承诺
    fun fulfillCommitment(commitmentId: String) {
        viewModelScope.launch {
            try {
                // 将承诺状态更新为已完成
                commitmentRepository.updateCommitmentStatus(commitmentId, CommitmentStatus.FULFILLED)
                
                // 通知用户承诺已完成
                _uiState.value = _uiState.value.copy(
                    errorMessage = null
                )
            } catch (e: Exception) {
                Timber.e(e, "完成承诺失败")
                _uiState.value = _uiState.value.copy(
                    errorMessage = "完成承诺失败: ${e.message}"
                )
            }
        }
    }
    
    // 清除错误消息
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    // 重置lastCreatedActionId
    fun resetLastCreatedActionId() {
        _uiState.value = _uiState.value.copy(
            lastCreatedActionId = null
        )
    }
    
    // 显示搜索对话框
    fun showSearchDialog() {
        Timber.d("显示搜索对话框")
        _uiState.value = _uiState.value.copy(
            isSearchActive = true,
            searchKeyword = "",
            searchResults = emptyList()
        )
    }
    
    // 隐藏搜索对话框
    fun hideSearchDialog() {
        Timber.d("隐藏搜索对话框")
        _uiState.value = _uiState.value.copy(
            isSearchActive = false,
            searchKeyword = "",
            searchResults = emptyList()
        )
        // 添加额外的日志，帮助调试
        Timber.d("搜索对话框已隐藏，isSearchActive = ${_uiState.value.isSearchActive}")
    }
    
    // 更新搜索关键字
    fun updateSearchKeyword(keyword: String) {
        Timber.d("更新搜索关键字: $keyword")
        _uiState.value = _uiState.value.copy(
            searchKeyword = keyword
        )
        // 立即执行搜索
        performSearch()
    }
    
    // 执行搜索
    fun performSearch() {
        viewModelScope.launch {
            val userId = _currentUserId.value ?: return@launch
            val keyword = _uiState.value.searchKeyword.trim()
            
            Timber.d("执行搜索，关键词: '$keyword'")
            
            if (keyword.isBlank()) {
                Timber.d("关键词为空，清空搜索结果")
                _uiState.value = _uiState.value.copy(
                    searchResults = emptyList()
                )
                return@launch
            }
            
            try {
                actionRepository.searchActionsByKeyword(userId, keyword).collect { actions ->
                    Timber.d("搜索到 ${actions.size} 个结果")
                    _uiState.value = _uiState.value.copy(
                        searchResults = actions
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "搜索行为失败: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "搜索失败: ${e.localizedMessage}",
                    searchResults = emptyList()
                )
            }
        }
    }
    
    companion object {
        private const val TAG = "ActionViewModel"
    }
}

// UI状态数据类
data class ActionUiState(
    val showActionDialog: Boolean = false,
    val actionDialogMode: ActionDialogMode = ActionDialogMode.ADD,
    val currentAction: Action? = null,
    val lastCreatedActionId: String? = null, // 最后创建的行为ID，用于添加承诺
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val availableGoals: List<Goal> = emptyList(), // 可用于关联的目标列表
    val isSearchActive: Boolean = false, // 是否激活搜索
    val searchKeyword: String = "", // 搜索关键字
    val searchResults: List<Action> = emptyList() // 搜索结果
)

// 播种页面数据
data class ActionScreenData(
    val pendingCommitments: List<Commitment> = emptyList(),
    val actions: List<Action> = emptyList(),
    val showDialog: Boolean = false,
    val dialogMode: ActionDialogMode = ActionDialogMode.ADD,
    val currentAction: Action? = null,
    val errorMessage: String? = null,
    val allCommitments: List<Commitment> = emptyList()
)

// 行为对话框模式
enum class ActionDialogMode {
    ADD,  // 添加行为
    VIEW  // 查看行为
} 