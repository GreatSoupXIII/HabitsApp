package com.example.habitsapp

import com.example.habitsapp.models.HabitHistory
import com.example.habitsapp.models.HabitHistoryEntry
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HabitHistoryTestFailureStreak {
    //check if getFailureStreak gets correct result when there are no entries but several days had already passed
    @Test
    fun failureStreak_isCorrect_None() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf()
        val history = HabitHistory(items, LocalDate.now().minusDays(3))

        assertEquals(2, history.getFailureStreak())
    }

    //check if getFailureStreak gets correct result when there is no failure streak
    @Test
    fun failureStreak_isCorrect_NoStreak() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(3, 1,LocalDate.now()),
            HabitHistoryEntry(2, 1,LocalDate.now().minusDays(1)),
            HabitHistoryEntry(1, 1, LocalDate.now().minusDays(2))
        )
        val history = HabitHistory(items, LocalDate.of(1970, 1, 1))

        assertEquals(0, history.getFailureStreak())
    }

    //check if getFailureStreak gets correct result when there is a failure
    @Test
    fun failureStreak_isCorrect_WithFailure() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(2, 1,LocalDate.now().minusDays(2)),
            HabitHistoryEntry(1, 1, LocalDate.now().minusDays(3))
        )
        val history = HabitHistory(items, LocalDate.of(1970, 1, 1))

        assertEquals(1, history.getFailureStreak())
    }

    @Test
    fun failureStreak_isCorrect_WithNoMarkTodayButYesterday() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(2, 1,LocalDate.now().minusDays(1)),
            HabitHistoryEntry(1, 1, LocalDate.now().minusDays(2))
        )
        val history = HabitHistory(items, LocalDate.of(1970, 1, 1))

        assertEquals(0, history.getFailureStreak())
    }
}