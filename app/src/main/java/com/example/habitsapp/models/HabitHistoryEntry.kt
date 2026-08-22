package com.example.habitsapp.models

import com.example.habitsapp.data.entities.HabitHistoryEntryData
import java.time.LocalDate

data class HabitHistoryEntry (
    val id: Int? = null,
    val habitId: Int,
    val date: LocalDate = LocalDate.now()
) {
    constructor(habitHistoryEntryData: HabitHistoryEntryData): this(
        habitHistoryEntryData.id,
        habitHistoryEntryData.habitId,
        LocalDate.parse(
            habitHistoryEntryData.date
        )
    )
}