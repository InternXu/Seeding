package com.example.seeding.ui.screens.goal

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seeding.data.model.Goal
import com.example.seeding.data.model.GoalStatus
import com.example.seeding.domain.model.User
import com.example.seeding.domain.repository.GoalRepository
import com.example.seeding.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 当前登录用户ID，实际应用中应该从用户会话获取
    // 这里为了演示暂时使用固定值
    // 使用可变状态流存储当前用户ID
    private val _currentUserId = MutableStateFlow<String?>(null)
    
    // UI状态
    private val _uiState = MutableStateFlow(GoalUiState())
    val uiState: StateFlow<GoalUiState> = _uiState.asStateFlow()

    // 所有目标
    private val _allGoals = MutableStateFlow<List<Goal>>(emptyList())
    
    // 根据筛选条件过滤后的目标
    val filteredGoals = combine(_allGoals, _uiState) { goals, state ->
        try {
            when (state.selectedFilter) {
                GoalFilter.ALL -> goals.filter { it.status !in listOf(GoalStatus.COMPLETED, GoalStatus.ABANDONED) }
                GoalFilter.COMPLETED -> goals.filter { it.status == GoalStatus.COMPLETED }
                GoalFilter.DELETED -> goals.filter { it.status == GoalStatus.ABANDONED }
                is GoalFilter.BySeed -> goals.filter { 
                    it.status !in listOf(GoalStatus.COMPLETED, GoalStatus.ABANDONED) && 
                    it.seedIds.contains((state.selectedFilter as GoalFilter.BySeed).seedId) 
                }
            }
        } catch (e: Exception) {
            // 如果过滤过程中出现异常，返回空列表，避免闪退
            emptyList()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        Log.d("GoalViewModel", "Initializing GoalViewModel")
        
        viewModelScope.launch {
            try {
                // 尝试获取当前登录用户
                val currentUser = userRepository.getCurrentUser().first()
                
                if (currentUser != null) {
                    Log.d("GoalViewModel", "Current user found: ${currentUser.id}")
                    _currentUserId.value = currentUser.id
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
                        Log.d("GoalViewModel", "Default user created: ${defaultUser.id}")
                    } catch (e: Exception) {
                        Log.e("GoalViewModel", "Error creating default user: ${e.message}", e)
                    }
                }
                
                // 确保有用户ID后再加载数据
                if (!_currentUserId.value.isNullOrEmpty()) {
                    // 加载目标数据
                    loadGoals()
                    
                    // 检查并更新逾期目标
                    checkForOverdueGoals()
                } else {
                    Log.e("GoalViewModel", "Cannot load goals: user ID is still null after initialization")
                }
            } catch (e: Exception) {
                Log.e("GoalViewModel", "Error during initial user setup: ${e.message}", e)
                // 根据需要设置错误状态
                _uiState.value = _uiState.value.copy(errorMessage = "初始化数据失败: ${e.localizedMessage}")
            }
        }
    }

    // 加载目标数据
    private fun loadGoals() {
        val userId = _currentUserId.value
        if (userId.isNullOrEmpty()) {
            Log.w("GoalViewModel", "无法加载目标：用户ID为空")
            return
        }
        
        viewModelScope.launch {
            try {
                goalRepository.getAllUserGoals(userId).collect { goals ->
                    _allGoals.value = goals
                }
            } catch (e: Exception) {
                Log.e("GoalViewModel", "Error loading goals: ${e.message}", e)
                _uiState.value = _uiState.value.copy(errorMessage = "加载目标失败: ${e.localizedMessage}")
            }
        }
    }

    // 检查逾期目标
    private fun checkForOverdueGoals() {
        val userId = _currentUserId.value
        if (userId.isNullOrEmpty()) {
            Log.w("GoalViewModel", "无法检查逾期目标：用户ID为空")
            return
        }
        
        viewModelScope.launch {
            try {
                goalRepository.checkForOverdueGoals(userId)
            } catch (e: Exception) {
                Log.e("GoalViewModel", "Error checking overdue goals: ${e.message}", e)
            }
        }
    }

    // 更新筛选条件
    fun updateFilter(filter: GoalFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    // 显示新增目标对话框
    fun showAddGoalDialog() {
        _uiState.value = _uiState.value.copy(
            showGoalDialog = true,
            goalDialogMode = GoalDialogMode.ADD,
            currentGoal = null
        )
    }

    // 显示编辑目标对话框
    fun showEditGoalDialog(goal: Goal) {
        _uiState.value = _uiState.value.copy(
            showGoalDialog = true,
            goalDialogMode = GoalDialogMode.EDIT,
            currentGoal = goal
        )
    }

    // 隐藏目标对话框
    fun hideGoalDialog() {
        _uiState.value = _uiState.value.copy(
            showGoalDialog = false,
            currentGoal = null
        )
    }

    // 显示删除确认对话框
    fun showDeleteConfirmDialog(goal: Goal) {
        _uiState.value = _uiState.value.copy(
            showDeleteConfirmDialog = true,
            currentGoal = goal
        )
    }

    // 隐藏删除确认对话框
    fun hideDeleteConfirmDialog() {
        _uiState.value = _uiState.value.copy(
            showDeleteConfirmDialog = false,
            currentGoal = null
        )
    }

    // 显示彻底删除确认对话框
    fun showPermanentDeleteDialog(goal: Goal) {
        _uiState.value = _uiState.value.copy(
            showPermanentDeleteDialog = true,
            currentGoal = goal
        )
    }

    // 隐藏彻底删除确认对话框
    fun hidePermanentDeleteDialog() {
        _uiState.value = _uiState.value.copy(
            showPermanentDeleteDialog = false,
            currentGoal = null
        )
    }

    // 显示完成目标确认对话框
    fun showCompleteConfirmDialog(goalId: String) {
        _uiState.value = _uiState.value.copy(
            showCompleteConfirmDialog = true,
            goalToComplete = goalId
        )
    }

    // 隐藏完成目标确认对话框
    fun hideCompleteConfirmDialog() {
        _uiState.value = _uiState.value.copy(
            showCompleteConfirmDialog = false,
            goalToComplete = null
        )
    }

    // 添加目标
    fun addGoal(title: String, description: String, deadline: Long, seedIds: List<Int>) {
        viewModelScope.launch {
            try {
                val userId = _currentUserId.value ?: return@launch
                
                val newGoal = Goal(
                    goalId = UUID.randomUUID().toString(),
                    userId = userId,
                    title = title,
                    description = description,
                    seedIds = seedIds,
                    deadline = deadline,
                    createdAt = System.currentTimeMillis(),
                    status = GoalStatus.IN_PROGRESS
                )
                goalRepository.createGoal(newGoal)
                hideGoalDialog()
            } catch (e: Exception) {
                Log.e("GoalViewModel", "Error adding goal: ${e.message}", e)
                _uiState.value = _uiState.value.copy(errorMessage = "添加目标失败: ${e.localizedMessage}")
            }
        }
    }

    // 更新目标
    fun updateGoal(goalId: String, description: String, deadline: Long) {
        viewModelScope.launch {
            try {
                val goal = goalRepository.getGoalById(goalId)
                if (goal != null) {
                    val updatedGoal = goal.copy(
                        description = description,
                        deadline = deadline
                    )
                    goalRepository.updateGoal(updatedGoal)
                }
                hideGoalDialog()
            } catch (e: Exception) {
                Log.e("GoalViewModel", "Error updating goal: ${e.message}", e)
                _uiState.value = _uiState.value.copy(errorMessage = "更新目标失败: ${e.localizedMessage}")
            }
        }
    }

    // 完成目标
    fun completeGoal(goalId: String) {
        _uiState.value = _uiState.value.copy(
            showCompleteConfirmDialog = true,
            goalToComplete = goalId
        )
    }

    // 确认完成目标
    fun confirmCompleteGoal() {
        viewModelScope.launch {
            try {
                _uiState.value.goalToComplete?.let { goalId ->
                    goalRepository.completeGoal(goalId)
                }
                hideCompleteConfirmDialog()
                hideGoalDialog() // 确保编辑窗口也关闭
            } catch (e: Exception) {
                Log.e("GoalViewModel", "Error completing goal: ${e.message}", e)
                _uiState.value = _uiState.value.copy(errorMessage = "完成目标失败: ${e.localizedMessage}")
            }
        }
    }

    // 删除目标（标记为放弃）
    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            try {
                goalRepository.abandonGoal(goalId)
                hideDeleteConfirmDialog()
                hideGoalDialog() // 确保编辑窗口也关闭
            } catch (e: Exception) {
                Log.e("GoalViewModel", "Error deleting goal: ${e.message}", e)
                _uiState.value = _uiState.value.copy(errorMessage = "删除目标失败: ${e.localizedMessage}")
            }
        }
    }

    // 彻底删除目标
    fun permanentDeleteGoal(goal: Goal) {
        viewModelScope.launch {
            try {
                goalRepository.deleteGoal(goal)
                hidePermanentDeleteDialog()
                hideGoalDialog() // 确保编辑窗口也关闭
            } catch (e: Exception) {
                Log.e("GoalViewModel", "Error permanently deleting goal: ${e.message}", e)
                _uiState.value = _uiState.value.copy(errorMessage = "彻底删除目标失败: ${e.localizedMessage}")
            }
        }
    }

    // 恢复目标
    fun restoreGoal(goalId: String) {
        viewModelScope.launch {
            try {
                val goal = goalRepository.getGoalById(goalId)
                if (goal != null) {
                    val updatedGoal = goal.copy(status = GoalStatus.IN_PROGRESS)
                    goalRepository.updateGoal(updatedGoal)
                }
            } catch (e: Exception) {
                Log.e("GoalViewModel", "Error restoring goal: ${e.message}", e)
                _uiState.value = _uiState.value.copy(errorMessage = "恢复目标失败: ${e.localizedMessage}")
            }
        }
    }

    // 切换编辑模式
    fun toggleEditMode() {
        _uiState.value = _uiState.value.copy(
            isEditMode = !_uiState.value.isEditMode
        )
    }
}

// UI状态数据类
data class GoalUiState(
    val selectedFilter: GoalFilter = GoalFilter.ALL,
    val showGoalDialog: Boolean = false,
    val goalDialogMode: GoalDialogMode = GoalDialogMode.ADD,
    val currentGoal: Goal? = null,
    val showDeleteConfirmDialog: Boolean = false,
    val showPermanentDeleteDialog: Boolean = false,
    val showCompleteConfirmDialog: Boolean = false,
    val goalToComplete: String? = null,
    val errorMessage: String? = null,
    val isEditMode: Boolean = false // 保留全局编辑模式标志
)

// 目标对话框模式
enum class GoalDialogMode {
    ADD, EDIT
}

// 目标筛选枚举
sealed class GoalFilter {
    object ALL : GoalFilter()
    data class BySeed(val seedId: Int) : GoalFilter()
    object COMPLETED : GoalFilter()
    object DELETED : GoalFilter()
} 