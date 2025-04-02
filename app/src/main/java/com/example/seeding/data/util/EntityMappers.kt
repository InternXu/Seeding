package com.example.seeding.data.util

import com.example.seeding.data.local.entity.GoalEntity
import com.example.seeding.data.local.entity.SeedEntity
import com.example.seeding.data.local.entity.UserEntity
import com.example.seeding.data.model.Goal
import com.example.seeding.data.model.Seed
import com.example.seeding.domain.model.User

/**
 * 实体-模型映射工具
 */
object EntityMappers {
    
    // 种子实体 -> 种子模型
    fun mapToSeed(entity: SeedEntity): Seed {
        return Seed(
            id = entity.id,
            name = entity.name,
            fullName = entity.fullName
        )
    }
    
    // 种子模型 -> 种子实体
    fun mapToSeedEntity(model: Seed): SeedEntity {
        return SeedEntity(
            id = model.id,
            name = model.name,
            fullName = model.fullName
        )
    }
    
    // 用户实体 -> 用户模型
    fun mapToUser(entity: UserEntity): User {
        return User(
            id = entity.id,
            username = entity.username,
            phoneNumber = entity.phone ?: "",
            email = entity.email ?: "",
            createdAt = entity.createdAt,
            lastLoginAt = entity.lastLoginAt ?: entity.createdAt
        )
    }
    
    // 用户模型 -> 用户实体
    fun mapToUserEntity(model: User): UserEntity {
        return UserEntity(
            id = model.id,
            username = model.username,
            phone = if (model.phoneNumber.isBlank()) null else model.phoneNumber,
            email = if (model.email.isBlank()) null else model.email,
            createdAt = model.createdAt,
            lastLoginAt = model.lastLoginAt
        )
    }
    
    // 目标实体 -> 目标模型
    fun mapToGoal(entity: GoalEntity): Goal {
        val seedIds = if (entity.seedIds.isNullOrEmpty()) {
            emptyList()
        } else {
            entity.seedIds.split(",").mapNotNull { 
                try { 
                    it.trim().toInt() 
                } catch (e: NumberFormatException) { 
                    null 
                }
            }
        }
        
        return Goal(
            goalId = entity.goalId,
            userId = entity.userId,
            title = entity.title,
            description = entity.description,
            seedIds = seedIds,
            deadline = entity.deadline,
            createdAt = entity.createdAt,
            status = entity.status
        )
    }
    
    // 目标模型 -> 目标实体
    fun mapToGoalEntity(model: Goal): GoalEntity {
        val seedIdsString = model.seedIds.joinToString(",")
        
        return GoalEntity(
            goalId = model.goalId,
            userId = model.userId,
            title = model.title,
            description = model.description,
            seedIds = seedIdsString,
            deadline = model.deadline,
            createdAt = model.createdAt,
            status = model.status,
            lastUpdated = System.currentTimeMillis(),
            isSynced = false
        )
    }
    
    // 目标实体列表 -> 目标模型列表
    fun mapToGoalList(entities: List<GoalEntity>): List<Goal> {
        return entities.map { mapToGoal(it) }
    }
} 