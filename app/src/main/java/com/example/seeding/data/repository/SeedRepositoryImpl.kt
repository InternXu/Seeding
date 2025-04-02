package com.example.seeding.data.repository

import com.example.seeding.data.local.dao.SeedDao
import com.example.seeding.data.model.Seed
import com.example.seeding.data.util.EntityMappers
import com.example.seeding.domain.repository.SeedRepository
import com.example.seeding.util.SeedUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedRepositoryImpl @Inject constructor(
    private val seedDao: SeedDao
) : SeedRepository {
    
    override fun getAllSeeds(): Flow<List<Seed>> {
        return seedDao.getAllSeeds().map { entities ->
            entities.map { EntityMappers.mapToSeed(it) }
        }
    }
    
    override suspend fun getSeedById(seedId: Int): Seed? {
        val entity = seedDao.getSeedById(seedId) ?: return null
        return EntityMappers.mapToSeed(entity)
    }
    
    override suspend fun initializeSeeds() {
        // 只有当数据库中没有种子数据时才初始化
        if (getSeedCount() == 0) {
            val seedModels = SeedUtils.getAllSeedModels()
            val seedEntities = seedModels.map { EntityMappers.mapToSeedEntity(it) }
            seedDao.insertSeeds(seedEntities)
        }
    }
    
    override suspend fun getSeedCount(): Int {
        return seedDao.getSeedCount()
    }
} 