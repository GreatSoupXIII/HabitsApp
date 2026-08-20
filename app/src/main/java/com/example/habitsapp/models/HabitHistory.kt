package com.example.habitsapp.models

import java.time.LocalDate
import java.time.Period

data class HabitHistory (
    val items: List<HabitHistoryEntry> = listOf<HabitHistoryEntry>(),
    val today: LocalDate = LocalDate.now()
) {
    //used to get how many consecutive times the user did a habit
    //counting until today
    fun getSuccessStreak(): Int {
        var count = 0
        for(i in items.indices) {
            count++
            if(i + 1 == items.size) break
            //if two dates are different not by one day (so, by more than one day)
            if(Period.between(items[i + 1].date, items[i].date) != Period.ofDays(1)) break
        }
        return count
    }

    //used to get how many consecutive times the user didn't do a habit
    //counting until today
    fun getFailureStreak(): Int {
        return if(items.isEmpty()) 0
            else Period.between(items[0].date, today).days
    }
}