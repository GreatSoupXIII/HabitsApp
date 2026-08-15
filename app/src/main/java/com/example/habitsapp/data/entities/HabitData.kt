package com.example.habitsapp.data.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.habitsapp.models.Habit

@Entity
data class HabitData(
    @PrimaryKey val id: Int?,
    val name: String,
    val successStreak: Int,
    var isReminderActive: Boolean,
    var hour: Int,
    var minute: Int
) {
    //used to create a HabitData object to add to the database
    constructor(habit: Habit): this(
        null,
        habit.name,
        habit.successStreak,
        habit.reminder.isActive,
        habit.reminder.hour,
        habit.reminder.minute
    )
}
