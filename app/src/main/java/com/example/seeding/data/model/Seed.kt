package com.example.seeding.data.model

/**
 * 表示十二大好种子
 */
enum class Seed(val id: Int, val title: String, val shortTitle: String, val description: String) {
    LIFE(1, "保护生命", "生命", "珍视并尊重一切生命形式，避免伤害自己和他人"),
    PROPERTY(2, "尊重他人财物", "财物", "不偷窃或侵占他人财产，尊重私有权"),
    RELATIONSHIP(3, "尊重他人的伴侣关系", "伴侣", "维护并尊重他人的情感关系和家庭纽带"),
    HONESTY(4, "诚实言语", "诚实", "在言语中保持真实和诚信，不欺骗他人"),
    HARMONY(5, "和谐言语", "和谐", "使用促进和谐与理解的言语，避免引起分裂和冲突"),
    GENTLENESS(6, "柔和言语", "柔和", "以柔和、温和的方式表达，避免伤害性言论"),
    MEANING(7, "有意义语言", "含义", "确保言语有益、有意义，避免空洞无益的交谈"),
    REJOICE(8, "随喜他人的成功", "随喜", "为他人的成就和幸福感到喜悦，远离嫉妒"),
    COMPASSION(9, "同情他人的不幸", "同情", "对他人的困难和痛苦表达同情与关怀"),
    MINDFULNESS(10, "维持正确的世界观", "正念", "了悟事物运作的真相，保持正确认知"),
    GRATITUDE(11, "感恩", "感恩", "对生活中的恩惠和帮助心存感激"),
    SURPRISE(12, "提前和惊喜", "提前", "做出超出期望的积极行动，给他人带来惊喜");

    companion object {
        fun getById(id: Int): Seed? = values().find { it.id == id }
    }
} 