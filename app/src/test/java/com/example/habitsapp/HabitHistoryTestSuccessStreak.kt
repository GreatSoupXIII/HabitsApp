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
        val history = HabitHistory(items, LocalDate.of(1970, 1, 1))

        assertEquals(0, history.getSuccessStreak())
    }

    //check if getSuccessStreak gets correct result when there are no gaps in entries
    @Test
    fun successStreak_isCorrect_NoGap() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(3, 1, LocalDate.now()),
            HabitHistoryEntry(2, 1, LocalDate.now().minusDays(1)),
            HabitHistoryEntry(1, 1, LocalDate.now().minusDays(2))
        )
        val history = HabitHistory(items, LocalDate.of(1970, 1, 1))

        assertEquals(3, history.getSuccessStreak())
    }

    //check if getSuccessStreak gets correct result when there is a gap in entries
    @Test
    fun successStreak_isCorrect_WithGap() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(5, 1,LocalDate.now()),
            HabitHistoryEntry(4, 1,LocalDate.now().minusDays(1)),
            HabitHistoryEntry(3, 1,LocalDate.now().minusDays(3)),
            HabitHistoryEntry(2, 1,LocalDate.now().minusDays(4)),
            HabitHistoryEntry(1, 1,LocalDate.now().minusDays(5))
        )
        val history = HabitHistory(items, LocalDate.of(1970, 1, 1))

        assertEquals(2, history.getSuccessStreak())
    }

    //check if getSuccessStreak gets correct result when there is a monthly gap in entries
    @Test
    fun successStreak_isCorrect_WithGapMonth() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(5, 1,LocalDate.now()),
            HabitHistoryEntry(4, 1,LocalDate.now().minusDays(1)),
            HabitHistoryEntry(3, 1,LocalDate.now().minusDays(2)),
            HabitHistoryEntry(2, 1,LocalDate.now().minusDays(3).minusMonths(1)),
            HabitHistoryEntry(1, 1,LocalDate.now().minusDays(4).minusMonths(1))
        )
        val history = HabitHistory(items, LocalDate.of(1970, 1, 1))

        assertEquals(3, history.getSuccessStreak())
    }

    //check if getSuccessStreak still gets streak when there is no entry for today
    //but with a mark for yesterday
    @Test
    fun successStreak_isCorrect_WithNoMarkTodayButYesterday() {
        val items: MutableList<HabitHistoryEntry> = mutableListOf(
            HabitHistoryEntry(4, 1, LocalDate.now().minusDays(1)),
            HabitHistoryEntry(3, 1, LocalDate.now().minusDays(2)),
            HabitHistoryEntry(2, 1, LocalDate.now().minusDays(4)),
            HabitHistoryEntry(1, 1, LocalDate.now().minusDays(5)),
        )
        val history = HabitHistory(items, LocalDate.of(1970, 1, 1))

        assertEquals(2, history.getSuccessStreak())
    }
}