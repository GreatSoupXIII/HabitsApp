package com.example.habitsapp

import com.example.habitsapp.data.HabitRepository

interface AppContainer {
    val repository: HabitRepository
}