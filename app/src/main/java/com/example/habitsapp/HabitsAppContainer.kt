package com.example.habitsapp

import android.content.Context
import com.example.habitsapp.data.AppRepository
import com.example.habitsapp.data.HabitRepository

class HabitsAppContainer(context: Context): AppContainer {
    override val repository: HabitRepository by lazy {
        AppRepository(context)
    }
}