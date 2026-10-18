package com.example.habitsapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.habitsapp.data.HabitRepository
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.HabitHistoryEntry
import kotlinx.coroutines.launch

class AppViewModel(private val repository: HabitRepository): ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val repository = application.container.repository
                AppViewModel(repository = repository)
            }
        }
    }

    val habitsList = mutableStateListOf<Habit>()

    init {
        repository.checkFailureStreaksAndNotify()
    }

    fun loadHabitList(): List<Habit> {
        viewModelScope.launch {
            val habitDataList = repository.loadHabitList()

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
            val id = repository.addHabitAndReload(habit)
            val habitWithId = habit
            habitWithId.id = id.toInt() //todo: temporary fix, need to change type from Int to Long in 'id' field in Habit.kt and HabitData.kt
            createReminder(habitWithId)
            loadHabitList()
        }
    }

    fun deleteHabitsAndReload(habits: List<Habit>) {
        viewModelScope.launch {
            repository.deleteHabitsAndReload(habits)
            for (habit in habits) {
                cancelReminder(habit.id!!)
            }
            loadHabitList()
        }
    }

    fun updateHabitAndReload(habit: Habit) {
        viewModelScope.launch {
            repository.updateHabitAndReload(habit)
            createReminder(habit)
            loadHabitList()
        }
    }

    fun addEntry(entry: HabitHistoryEntry) {
        viewModelScope.launch {
            repository.addEntry(entry)
            loadHabitList()
        }
    }

    fun deleteEntry(entry: HabitHistoryEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
            loadHabitList()
        }
    }

    fun createReminder(habit: Habit) {
        repository.createReminder(habit)
    }

    fun cancelReminder(habitId: Int) {
        repository.cancelReminder(habitId)
    }

}