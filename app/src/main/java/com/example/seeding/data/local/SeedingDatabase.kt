package com.example.seeding.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.seeding.data.local.dao.ActionDao
import com.example.seeding.data.local.dao.CommitmentDao
import com.example.seeding.data.local.dao.GoalDao
import com.example.seeding.data.local.dao.SeedDao
import com.example.seeding.data.local.dao.UserDao
import com.example.seeding.data.local.entity.ActionEntity
import com.example.seeding.data.local.entity.CommitmentEntity
import com.example.seeding.data.local.entity.GoalEntity
import com.example.seeding.data.local.entity.SeedEntity
import com.example.seeding.data.local.entity.UserEntity
import com.example.seeding.util.SeedUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        SeedEntity::class,
        GoalEntity::class,
        ActionEntity::class,
        CommitmentEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SeedingDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun seedDao(): SeedDao
    abstract fun goalDao(): GoalDao
    abstract fun actionDao(): ActionDao
    abstract fun commitmentDao(): CommitmentDao

    companion object {
        @Volatile
        private var INSTANCE: SeedingDatabase? = null

        fun getDatabase(context: Context): SeedingDatabase {
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
                                    val seedEntities = SeedUtils.getAllSeedModels().map { seed ->
                                        SeedEntity(
                                            id = seed.id,
                                            name = seed.name,
                                            fullName = seed.fullName
                                        )
                                    }
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