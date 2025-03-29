package com.example.seeding.data.local

import androidx.room.TypeConverter
import com.example.seeding.data.model.ProgressMilestone
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room数据库类型转换器
 */
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromIntList(value: List<Int>?): String {
        return gson.toJson(value ?: emptyList<Int>())
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value ?: emptyList<String>())
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromProgressMilestoneList(value: List<ProgressMilestone>?): String {
        return gson.toJson(value ?: emptyList<ProgressMilestone>())
    }

    @TypeConverter
    fun toProgressMilestoneList(value: String): List<ProgressMilestone> {
        val listType = object : TypeToken<List<ProgressMilestone>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }
} 