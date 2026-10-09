package com.example.habitsapp.models

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

data class HabitHistory (
    val items: List<HabitHistoryEntry> = listOf<HabitHistoryEntry>(),
    val habitCreatedAtDate: LocalDate
) {
    //used to get how many consecutive times the user did a habit
    //counting until today
    fun getSuccessStreak(): Int {
        var count = 0
        var extra = 0
        if(items.isEmpty()) return count
        //if there is no mark today, it should still display the streak
        //so it adds an extra 1 to period check if today mark is missing
        if(items[0].date != LocalDate.now()) extra = 1
        for(i in items.indices) {
            if(
                ChronoUnit.DAYS.between(
                    items[i].date,
                    LocalDate.now()
                ).days.toInt(DurationUnit.DAYS) != i + extra
            ) break
            count++
        }
        return count
    }

    //used to get how many consecutive times the user didn't do a habit
    //counting until today EXCLUDING
    //if there are no entires at all, it uses the date the habit was created at
    fun getFailureStreak(): Int {

        return if(items.isEmpty()) ChronoUnit.DAYS.between(
            habitCreatedAtDate,
            LocalDate.now().minusDays(1)
            ).days.toInt(DurationUnit.DAYS)
            else if(items[0].date == LocalDate.now()) 0
            else ChronoUnit.DAYS.between(
            items[0].date,
            LocalDate.now().minusDays(1)
            ).days.toInt(DurationUnit.DAYS)
    }
}