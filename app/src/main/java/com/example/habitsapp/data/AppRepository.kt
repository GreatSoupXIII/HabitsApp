package com.example.habitsapp.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.data.entities.HabitHistoryEntryData
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.HabitHistoryEntry
import com.example.habitsapp.reminder.ReminderReceiver
import com.example.habitsapp.worker.StreakUpdateWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

class AppRepository(
    val context: Context
) : HabitRepository {

    val database: AppDatabase = Room.databaseBuilder<AppDatabase>(context, "app-database")
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .build()

    override val habitDao = database.habitDao()
    override val habitHistoryEntryDao = database.habitHistoryEntryDao()

    override val workManager = WorkManager.getInstance(context)

    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override suspend fun loadHabitList(): Map<HabitData, List<HabitHistoryEntryData>> = habitDao.getAll()

    override suspend fun loadOnlyHabits(): List<HabitData> = habitDao.getHabits()

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

    override fun createReminder(habit: Habit) {

        val alarmIntent = Intent(context, ReminderReceiver::class.java)
            .putExtra("EXTRA_HABIT_ID", habit.id!!)
            .putExtra("EXTRA_HABIT_NAME", habit.name)
            .putExtra("EXTRA_HABIT_HOUR", habit.reminder.hour)
            .putExtra("EXTRA_HABIT_MINUTE", habit.reminder.minute)
            .let { intent ->
                PendingIntent.getBroadcast(
                    context,
                    habit.id!!,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, habit.reminder.hour)
            set(Calendar.MINUTE, habit.reminder.minute)
            set(Calendar.SECOND, 0)
            //if alarm time ends up before current time, schedule for next day
            if(timeInMillis < System.currentTimeMillis()) add(Calendar.DAY_OF_MONTH, 1)
        }

        //if app needs permission and doesn't have it, throw an exception
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmMgr.canScheduleExactAlarms()) {
            //todo: notify that result is bad, probably with a throw
            return //this is a temp fix
        }

        //otherwise it's good
        alarmMgr.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
    }

    override fun cancelReminder(habitId: Int) {
        val alarmIntent = Intent(context, ReminderReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, habitId, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        alarmMgr.cancel(alarmIntent)
    }

    override fun checkFailureStreaksAndNotify() {

        val workRequestBuilder = PeriodicWorkRequestBuilder<StreakUpdateWorker>(24, TimeUnit.HOURS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "Advice",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequestBuilder
        )
    }
}