package com.example.habitsapp.models

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

data class HabitHistory (
    val items: List<HabitHistoryEntry> = listOf<HabitHistoryEntry>(),
    val today: LocalDate = LocalDate.now()
) {
    //used to get how many consecutive times the user did a habit
    //counting until today
    fun getSuccessStreak(): Int {
        var count = 0
        var extra = 0
        if(items.isEmpty()) return count
        //if there is no mark today, it should still display the streak
        //so it adds an extra 1 to period check if today mark is missing
        if(items[0].date != today) extra = 1
        for(i in items.indices) {
            if(
                ChronoUnit.DAYS.between(
                    items[i].date,
                    today
                ).days.toInt(DurationUnit.DAYS) != i + extra
            ) break
            count++
        }
        return count
    }

    //used to get how many consecutive times the user didn't do a habit
    //counting until today EXCLUDING
    fun getFailureStreak(): Int {
        return if(items.isEmpty() || items[0].date == today) 0
            else ChronoUnit.DAYS.between(
            items[0].date,
            today.minusDays(1)
            ).days.toInt(DurationUnit.DAYS)
    }
}