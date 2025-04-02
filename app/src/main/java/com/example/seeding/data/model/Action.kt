package com.example.seeding.data.model

/**
 * 行为类型枚举
 */
enum class ActionType {
    POSITIVE, // 正面行为
    NEGATIVE, // 负面行为
    NEUTRAL   // 中性行为
}

/**
 * 行为数据模型，用于表示用户的行为记录
 */
data class Action(
    val actionId: String,           // 行为ID
    val userId: String = "",        // 用户ID
    val content: String,            // 行为内容
    val type: ActionType,           // 行为类型
    val timestamp: Long = System.currentTimeMillis(), // 创建时间
    val seedIds: List<Int> = emptyList(),         // 关联的种子ID列表
    val goalIds: List<String> = emptyList(),        // 关联的目标ID列表
    val hasCommitment: Boolean = false,              // 是否有承诺 (通常负面行为会有承诺)
    @Transient var tag: Any? = null
) 