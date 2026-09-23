package com.example.habitsapp.data

import androidx.work.WorkManager
import com.example.habitsapp.data.dao.HabitDao
import com.example.habitsapp.data.dao.HabitHistoryEntryDao
import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.data.entities.HabitHistoryEntryData
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.HabitHistoryEntry

interface HabitRepository {

    val habitDao: HabitDao
    val habitHistoryEntryDao: HabitHistoryEntryDao

    val workManager: WorkManager

    suspend fun loadHabitList(): Map<HabitData, List<HabitHistoryEntryData>>

    suspend fun addHabitAndReload(habit: Habit)

    suspend fun deleteHabitsAndReload(habits: List<Habit>)

    suspend fun updateHabitAndReload(habit: Habit)

    suspend fun addEntry(entry: HabitHistoryEntry)

    suspend fun deleteEntry(entry: HabitHistoryEntry)

    //this will make a repeating notification for a habit
    fun createNotification()

    //this will cancel a previously made notification for a habit
    fun cancelNotification()

    //this will check failure streaks
    //and make notifications if there are any that
    //satisfy the condition
    fun checkFailureStreaksAndNotify()
}