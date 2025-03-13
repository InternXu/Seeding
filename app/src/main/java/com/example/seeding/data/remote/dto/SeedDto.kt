package com.example.seeding.data.remote.dto

/**
 * 种子DTO
 */
data class SeedDto(
    val seedId: Int,
    val name: String,
    val description: String,
    val category: String,
    val parentSeedId: Int?,
    val isActive: Boolean
) 