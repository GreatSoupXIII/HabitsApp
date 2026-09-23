package com.example.habitsapp.data

import android.content.Context
import androidx.work.WorkManager
import com.example.habitsapp.data.dao.HabitDao
import com.example.habitsapp.data.dao.HabitHistoryEntryDao
import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.data.entities.HabitHistoryEntryData
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.HabitHistoryEntry

class AppRepository(
    override val habitDao: HabitDao,
    override val habitHistoryEntryDao: HabitHistoryEntryDao,
    context: Context
) : HabitRepository {

    override val workManager = WorkManager.getInstance(context)

    override suspend fun loadHabitList(): Map<HabitData, List<HabitHistoryEntryData>> = habitDao.getAll()

    override suspend fun addHabitAndReload(habit: Habit) = habitDao.insert(HabitData(habit))

    override suspend fun deleteHabitsAndReload(habits: List<Habit>) {
        val ids = habits.map {
            it.id!!
        }
        habitDao.delete(ids)
        habitHistoryEntryDao.deleteByHabitIds(ids)
    }

    override suspend fun updateHabitAndReload(habit: Habit) {
        habitDao.update(HabitData(habit))
    }

    override suspend fun addEntry(entry: HabitHistoryEntry) {
        habitHistoryEntryDao.insert(HabitHistoryEntryData(entry))
    }

    override suspend fun deleteEntry(entry: HabitHistoryEntry) {
        habitHistoryEntryDao.delete(HabitHistoryEntryData(entry))
    }

    //todo: make it create a repeating notification
    override fun createNotification() {

    }

    //todo: make it cancel a repeating notification
    override fun cancelNotification() {

    }

    //todo: make it do what the name says
    override fun checkFailureStreaksAndNotify() {

    }

}