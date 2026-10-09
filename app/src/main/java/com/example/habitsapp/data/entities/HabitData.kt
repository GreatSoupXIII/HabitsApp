package com.example.habitsapp.data.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.habitsapp.models.Habit
import java.time.format.DateTimeFormatter

@Entity
data class HabitData(
    @PrimaryKey val id: Int?,
    val name: String,
    var isReminderActive: Boolean,
    var hour: Int,
    var minute: Int,
    val createdAtDate: String
) {
    //used to create a HabitData object to add to the database
    constructor(habit: Habit): this(
        habit.id,
        habit.name,
        habit.reminder.isActive,
        habit.reminder.hour,
        habit.reminder.minute,
        habit.history.habitCreatedAtDate.format(DateTimeFormatter.ISO_DATE)
    )
}
