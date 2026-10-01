package com.example.habitsapp.data

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.data.entities.HabitHistoryEntryData
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.HabitHistoryEntry
import com.example.habitsapp.worker.StreakUpdateWorker
import java.util.concurrent.TimeUnit

class AppRepository(
    context: Context
) : HabitRepository {

    val database: AppDatabase = Room.databaseBuilder<AppDatabase>(context, "app-database")
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .build()

    override val habitDao = database.habitDao()
    override val habitHistoryEntryDao = database.habitHistoryEntryDao()

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

        //todo: change repeat interval from 1 minute to 1 day
        val workRequestBuilder = PeriodicWorkRequestBuilder<StreakUpdateWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(1, TimeUnit.DAYS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "Advice",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequestBuilder
        )
    }

}