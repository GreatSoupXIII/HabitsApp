package com.example.habitsapp.models

import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.data.entities.HabitHistoryEntryData
import java.time.LocalDate

data class Habit (
    var id: Int?,
    var name: String,
    var history: HabitHistory,
    var reminder: Reminder
) {
    //used to extract data from a HabitData object
    constructor (
        habitData: HabitData,
        historyEntries: List<HabitHistoryEntryData>
    ) : this(
        habitData.id,
        habitData.name,
        HabitHistory(
            historyEntries.map {
                HabitHistoryEntry(it.id, it.habitId, LocalDate.parse(it.date))
            }
        ),
        Reminder(
            habitData.isReminderActive,
            habitData.hour,
            habitData.minute
        )
    )
}