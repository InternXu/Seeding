package com.example.seeding.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.seeding.data.model.Action
import com.example.seeding.data.model.Goal
import com.example.seeding.data.model.Promise
import com.example.seeding.data.model.User

@Database(
    entities = [User::class, Goal::class, Promise::class, Action::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SeedingDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun goalDao(): GoalDao
    abstract fun promiseDao(): PromiseDao
    abstract fun actionDao(): ActionDao

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
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 