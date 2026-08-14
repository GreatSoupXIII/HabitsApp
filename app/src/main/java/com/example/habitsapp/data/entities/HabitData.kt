package com.example.habitsapp.data.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class HabitData(
    @PrimaryKey val id: Int,
    val name: String,
    val successStreak: Int,
    var isReminderActive: Boolean,
    var hour: Int,
    var minute: Int
)
