package com.example.habitsapp.models

import com.example.habitsapp.data.entities.HabitData

data class Habit (
    var id: Int?,
    var name: String,
    var successStreak: Int,
    var reminder: Reminder
) {
    //used to extract data from a HabitData object
    constructor (habitData: HabitData) : this(
        habitData.id,
        habitData.name,
        habitData.successStreak,
        Reminder(
            habitData.isReminderActive,
            habitData.hour,
            habitData.minute
        )
    )
}