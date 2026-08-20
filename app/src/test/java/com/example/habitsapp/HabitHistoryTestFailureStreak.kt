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
    //check if getFailureStreak gets correct result when there are no entries
    @Test
    fun failureStreak_isCorrect_None() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf()
        val history = HabitHistory(items, LocalDate.of(2026, 8, 20))

        assertEquals(0, history.getFailureStreak())
    }

    //check if getFailureStreak gets correct result when there are no failures
    @Test
    fun failureStreak_isCorrect_NoFailures() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(3, LocalDate.of(2026, 8, 20)),
            HabitHistoryEntry(2, LocalDate.of(2026, 8, 19)),
            HabitHistoryEntry(1, LocalDate.of(2026, 8, 18))
        )
        val history = HabitHistory(items)

        assertEquals(0, history.getFailureStreak())
    }

    //check if getFailureStreak gets correct result when there is a failure
    @Test
    fun failureStreak_isCorrect_WithFailure() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(1, LocalDate.of(2026, 8, 18)),
            HabitHistoryEntry(2, LocalDate.of(2026, 8, 16)),
            HabitHistoryEntry(2, LocalDate.of(2026, 8, 15)),
            HabitHistoryEntry(1, LocalDate.of(2026, 8, 14))
        )
        val history = HabitHistory(items)

        assertEquals(2, history.getFailureStreak())
    }
}