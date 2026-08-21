package com.example.habitsapp.models

import com.example.habitsapp.data.entities.HabitHistoryEntryData
import java.time.LocalDate

data class HabitHistoryEntry (
    val id: Int? = null,
    val date: LocalDate = LocalDate.now()
) {
    constructor(habitHistoryEntryData: HabitHistoryEntryData): this(
        habitHistoryEntryData.id,
        LocalDate.of(
            habitHistoryEntryData.year,
            habitHistoryEntryData.month,
            habitHistoryEntryData.day
        )
    )
}