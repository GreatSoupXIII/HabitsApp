package com.example.habitsapp

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.habitsapp.data.AppDatabase
import com.example.habitsapp.data.dao.HabitDao
import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.models.Habit
import kotlinx.coroutines.launch

class AppViewModel(application: Application): AndroidViewModel(application) {
    private val applicationContext = getApplication<Application>().applicationContext
    val database: AppDatabase = Room.databaseBuilder<AppDatabase>(applicationContext, "app-database")
        .setDriver(BundledSQLiteDriver())
        .build()
    val habitDao: HabitDao = database.habitDao()
    val habitsList = mutableStateListOf<Habit>()

    fun loadHabitList(): List<Habit> {
        viewModelScope.launch {
            val habitDataList = habitDao.getAll()

            habitsList.clear()
            //translate all HabitData objects from the database
            //to format used by the application
            for(habitData: HabitData in habitDataList) {
                habitsList.add(Habit(habitData))
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

    fun deleteHabitAndReload(habit: Habit) {
        viewModelScope.launch {
            habitDao.delete(HabitData(habit))
            loadHabitList()
        }
    }

    fun updateHabitAndReload(habit: Habit) {
        viewModelScope.launch {
            habitDao.update(HabitData(habit))
            loadHabitList()
        }
    }
}