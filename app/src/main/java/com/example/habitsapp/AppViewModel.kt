package com.example.habitsapp

import android.app.Application
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.habitsapp.data.AppDatabase
import com.example.habitsapp.data.dao.HabitDao
import com.example.habitsapp.data.dao.HabitHistoryEntryDao
import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.data.entities.HabitHistoryEntryData
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.HabitHistoryEntry
import com.example.habitsapp.notifications.NotificationsManager
import kotlinx.coroutines.launch
import java.util.jar.Manifest

class AppViewModel(application: Application): AndroidViewModel(application) {
    private val applicationContext = getApplication<Application>().applicationContext
    val database: AppDatabase = Room.databaseBuilder<AppDatabase>(applicationContext, "app-database")
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .build()
    val habitDao: HabitDao = database.habitDao()
    val habitHistoryEntryDao: HabitHistoryEntryDao = database.habitHistoryEntryDao()

    val habitsList = mutableStateListOf<Habit>()


    val notificationsManager = NotificationsManager()

    init {
        notificationsManager.createAdviceChannel(applicationContext)
        notificationsManager.createNotificationChannel(applicationContext)

        //todo: test action, remove it
        val builder = NotificationCompat.Builder(applicationContext, notificationsManager.ADVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_account_box)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            // notificationId is a unique int for each notification that you must define.
            notify(notificationsManager.getNewAdviceId(), builder.build())
        }
    }

    fun loadHabitList(): List<Habit> {
        viewModelScope.launch {
            val habitDataList = habitDao.getAll()

            habitsList.clear()
            //translate all HabitData objects from the database
            //to format used by the application
            habitDataList.map {
                habitsList.add(Habit(it.key, it.value))
            }
        }
        return habitsList
    }

    fun addHabitAndReload(habit: Habit) {
        viewModelScope.launch {
            habitDao.insert(HabitData(habit))
            loadHabitList()
        }
    }

    fun deleteHabitsAndReload(habits: List<Habit>) {
        val ids = habits.map {
            it.id!!
        }

        viewModelScope.launch {
            habitDao.delete(ids)
            habitHistoryEntryDao.deleteByHabitIds(ids)
            loadHabitList()
        }
    }

    fun updateHabitAndReload(habit: Habit) {
        viewModelScope.launch {
            habitDao.update(HabitData(habit))
            loadHabitList()
        }
    }

    fun addEntry(entry: HabitHistoryEntry) {
        viewModelScope.launch {
            habitHistoryEntryDao.insert(HabitHistoryEntryData(entry))
            loadHabitList()
        }
    }

    fun deleteEntry(entry: HabitHistoryEntry) {
        viewModelScope.launch {
            habitHistoryEntryDao.delete(HabitHistoryEntryData(entry))
            loadHabitList()
        }
    }
}