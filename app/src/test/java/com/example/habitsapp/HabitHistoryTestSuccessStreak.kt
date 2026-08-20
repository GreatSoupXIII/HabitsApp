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
class HabitHistoryTestSuccessStreak {
    //check if getSuccessStreak gets correct result when there are no entries
    @Test
    fun successStreak_isCorrect_None() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf()
        val history = HabitHistory(items, LocalDate.of(2026, 8, 20))

        assertEquals(0, history.getSuccessStreak())
    }

    //check if getSuccessStreak gets correct result when there are no gaps in entries
    @Test
    fun successStreak_isCorrect_NoGap() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(3, LocalDate.of(2026, 8, 20)),
            HabitHistoryEntry(2, LocalDate.of(2026, 8, 19)),
            HabitHistoryEntry(1, LocalDate.of(2026, 8, 18))
        )
        val history = HabitHistory(items)

        assertEquals(3, history.getSuccessStreak())
    }

    //check if getSuccessStreak gets correct result when there is a gap in entries
    @Test
    fun successStreak_isCorrect_WithGap() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(3, LocalDate.of(2026, 8, 20)),
            HabitHistoryEntry(2, LocalDate.of(2026, 8, 19)),
            HabitHistoryEntry(2, LocalDate.of(2026, 8, 17)),
            HabitHistoryEntry(2, LocalDate.of(2026, 8, 16)),
            HabitHistoryEntry(1, LocalDate.of(2026, 8, 15))
        )
        val history = HabitHistory(items)

        assertEquals(2, history.getSuccessStreak())
    }

    //check if getSuccessStreak gets correct result when there is a monthly gap in entries
    @Test
    fun successStreak_isCorrect_WithGapMonth() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(3, LocalDate.of(2026, 8, 20)),
            HabitHistoryEntry(2, LocalDate.of(2026, 8, 19)),
            HabitHistoryEntry(2, LocalDate.of(2026, 8, 18)),
            HabitHistoryEntry(2, LocalDate.of(2026, 7, 17)),
            HabitHistoryEntry(1, LocalDate.of(2026, 7, 16))
        )
        val history = HabitHistory(items)

        assertEquals(3, history.getSuccessStreak())
    }
}