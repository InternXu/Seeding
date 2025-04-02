package com.example.seeding.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.seeding.data.local.converter.Converters
import com.example.seeding.data.local.dao.GoalDao
import com.example.seeding.data.local.dao.SeedDao
import com.example.seeding.data.local.dao.UserDao
import com.example.seeding.data.local.entity.GoalEntity
import com.example.seeding.data.local.entity.SeedEntity
import com.example.seeding.data.local.entity.UserEntity
import com.example.seeding.util.SeedUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 应用主数据库
 */
@Database(
    entities = [
        UserEntity::class,
        SeedEntity::class,
        GoalEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SeedingDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun seedDao(): SeedDao
    abstract fun goalDao(): GoalDao
    
    companion object {
        @Volatile
        private var INSTANCE: SeedingDatabase? = null
        
        fun getInstance(context: Context): SeedingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SeedingDatabase::class.java,
                    "seeding_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // 在数据库创建时预填充种子数据
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val seedDao = database.seedDao()
                                
                                // 创建种子实体列表
                                val seedEntities = SeedUtils.getAllSeedModels().map { seed ->
                                    SeedEntity(
                                        id = seed.id,
                                        name = seed.name,
                                        fullName = seed.fullName,
                                        createdAt = System.currentTimeMillis()
                                    )
                                }
                                
                                // 插入种子数据
                                seedDao.insertSeeds(seedEntities)
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 