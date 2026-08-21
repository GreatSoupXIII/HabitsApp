package com.example.habitsapp.data.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.habitsapp.models.HabitHistoryEntry

@Entity
data class HabitHistoryEntryData(
    @PrimaryKey val id: Int?,
    val day: Int,
    val month: Int,
    val year: Int
) {
    //used to create a HabitHistoryEntryData object to add to the database
    constructor(habitHistoryEntry: HabitHistoryEntry): this(
        habitHistoryEntry.id,
        habitHistoryEntry.date.dayOfMonth,
        habitHistoryEntry.date.month.value,
        habitHistoryEntry.date.year
    )
}