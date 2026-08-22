package com.example.habitsapp.data.entities

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import com.example.habitsapp.models.HabitHistoryEntry
import java.time.format.DateTimeFormatter
import kotlin.arrayOf

//this entity has indices to make a UNIQUE constraint for both
//"habitId" and "date"
@Entity(indices = [ Index(value = ["habitId", "date"], unique = true) ] )
data class HabitHistoryEntryData(
    @PrimaryKey val id: Int?,
    val habitId: Int,
    val date: String
) {
    //used to create a HabitHistoryEntryData object to add to the database
    constructor(habitHistoryEntry: HabitHistoryEntry): this(
        habitHistoryEntry.id,
        habitHistoryEntry.habitId,
        habitHistoryEntry.date.format(DateTimeFormatter.ISO_DATE_TIME)
    )
}