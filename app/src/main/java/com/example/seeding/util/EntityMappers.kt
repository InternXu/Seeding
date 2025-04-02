package com.example.seeding.util

import com.example.seeding.data.local.entity.ActionEntity
import com.example.seeding.data.local.entity.CommitmentEntity
import com.example.seeding.data.local.entity.GoalEntity
import com.example.seeding.data.local.entity.SeedEntity
import com.example.seeding.data.local.entity.UserEntity
import com.example.seeding.data.model.Action
import com.example.seeding.data.model.Commitment
import com.example.seeding.data.model.Goal
import com.example.seeding.data.model.Seed
import com.example.seeding.domain.model.User

/**
 * 实体-模型映射工具类
 */
object EntityMappers {
    // ---------- 目标映射 ----------
    fun mapToGoal(entity: GoalEntity): Goal {
        val seedIds = if (entity.seedIds.isNullOrEmpty()) {
            emptyList()
        } else {
            entity.seedIds.split(",").mapNotNull { 
                try { it.trim().toInt() } catch (e: NumberFormatException) { null }
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
    
    fun mapToGoalList(entities: List<GoalEntity>): List<Goal> {
        return entities.map { mapToGoal(it) }
    }
    
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
    
    // ---------- 用户映射 ----------
    fun mapToUser(entity: UserEntity): User {
        return User(
            id = entity.id,
            username = entity.username,
            email = entity.email ?: "",
            phoneNumber = entity.phone ?: "",
            createdAt = entity.createdAt,
            lastLoginAt = entity.lastLoginAt ?: 0
        )
    }
    
    fun mapToUserEntity(model: User): UserEntity {
        return UserEntity(
            id = model.id,
            username = model.username,
            email = model.email,
            phone = model.phoneNumber,
            passwordHash = null, // 密码不通过映射传递
            createdAt = model.createdAt,
            lastLoginAt = model.lastLoginAt
        )
    }
    
    // ---------- 种子映射 ----------
    fun mapToSeed(entity: SeedEntity): Seed {
        return Seed(
            id = entity.id,
            name = entity.name,
            fullName = entity.fullName
        )
    }
    
    fun mapToSeedList(entities: List<SeedEntity>): List<Seed> {
        return entities.map { mapToSeed(it) }
    }
    
    fun mapToSeedEntity(model: Seed): SeedEntity {
        return SeedEntity(
            id = model.id,
            name = model.name,
            fullName = model.fullName
        )
    }
    
    // ---------- 行为映射 ----------
    fun mapToAction(entity: ActionEntity): Action {
        val seedIds = if (entity.seedIds.isNullOrEmpty()) {
            emptyList()
        } else {
            entity.seedIds.split(",").mapNotNull { 
                try { it.trim().toInt() } catch (e: NumberFormatException) { null }
            }
        }
        
        val goalIds = if (entity.goalIds.isNullOrEmpty()) {
            emptyList()
        } else {
            entity.goalIds.split(",").map { it.trim() }
        }
        
        return Action(
            actionId = entity.actionId,
            userId = entity.userId,
            content = entity.content,
            type = entity.type,
            seedIds = seedIds,
            goalIds = goalIds,
            timestamp = entity.createdAt,
            hasCommitment = entity.hasCommitment
        )
    }
    
    fun mapToActionEntity(model: Action): ActionEntity {
        val seedIdsString = model.seedIds.joinToString(",")
        val goalIdsString = model.goalIds.joinToString(",")
        
        return ActionEntity(
            actionId = model.actionId,
            userId = model.userId,
            content = model.content,
            type = model.type,
            seedIds = seedIdsString,
            goalIds = goalIdsString,
            createdAt = model.timestamp,
            hasCommitment = model.hasCommitment
        )
    }
    
    // ---------- 承诺映射 ----------
    fun mapToCommitment(entity: CommitmentEntity): Commitment {
        val seedIds = if (entity.seedIds.isNullOrEmpty()) {
            emptyList()
        } else {
            entity.seedIds.split(",").mapNotNull { 
                try { it.trim().toInt() } catch (e: NumberFormatException) { null }
            }
        }
        
        return Commitment(
            commitmentId = entity.commitmentId,
            actionId = entity.actionId,
            userId = entity.userId,
            content = entity.content,
            seedIds = seedIds,
            timeFrame = entity.timeFrame,
            timestamp = entity.createdAt,
            deadline = entity.deadline,
            status = entity.status
        )
    }
    
    fun mapToCommitmentEntity(model: Commitment): CommitmentEntity {
        val seedIdsString = model.seedIds.joinToString(",")
        
        return CommitmentEntity(
            commitmentId = model.commitmentId,
            actionId = model.actionId,
            userId = model.userId,
            content = model.content,
            seedIds = seedIdsString,
            timeFrame = model.timeFrame,
            createdAt = model.timestamp,
            deadline = model.deadline,
            status = model.status
        )
    }
} 