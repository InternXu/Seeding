package com.example.seeding.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.seeding.data.local.dao.BehaviorDao
import com.example.seeding.data.local.dao.SeedDao
import com.example.seeding.data.local.dao.TargetDao
import com.example.seeding.data.local.dao.UserDao
import com.example.seeding.data.local.entity.BehaviorEntity
import com.example.seeding.data.local.entity.BehaviorTargetCrossRef
import com.example.seeding.data.local.entity.SeedEntity
import com.example.seeding.data.local.entity.TargetEntity
import com.example.seeding.data.local.entity.TargetProgressEntity
import com.example.seeding.data.local.entity.UserEntity
import com.example.seeding.data.local.util.Converters

/**
 * Room数据库主类，定义所有实体和DAO
 */
@Database(
    entities = [
        UserEntity::class,
        SeedEntity::class,
        TargetEntity::class,
        BehaviorEntity::class,
        TargetProgressEntity::class,
        BehaviorTargetCrossRef::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class SeedingDatabase : RoomDatabase() {
    
    /**
     * 用户DAO
     */
    abstract fun userDao(): UserDao
    
    /**
     * 种子DAO
     */
    abstract fun seedDao(): SeedDao
    
    /**
     * 目标DAO
     */
    abstract fun targetDao(): TargetDao
    
    /**
     * 行为DAO
     */
    abstract fun behaviorDao(): BehaviorDao
} 