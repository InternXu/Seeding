package com.example.seeding.data.model

/**
 * 承诺状态枚举
 */
enum class CommitmentStatus {
    PENDING,     // 待履行
    UNFULFILLED, // 未履行
    FULFILLED,   // 已履行
    EXPIRED      // 已过期
}

/**
 * 承诺数据模型，表示用户针对负面行为做出的承诺
 */
data class Commitment(
    val commitmentId: String,       // 承诺ID
    val actionId: String,           // 关联的行为ID
    val userId: String = "",        // 用户ID
    val content: String,            // 承诺内容
    val timestamp: Long,            // 创建时间
    val deadline: Long,             // 截止时间
    val seedIds: List<Int>,         // 关联的种子ID列表
    val timeFrame: Int = 0,         // 时间框架（分钟）
    val status: CommitmentStatus = CommitmentStatus.PENDING // 承诺状态
) 