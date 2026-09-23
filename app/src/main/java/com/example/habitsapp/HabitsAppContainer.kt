package com.example.habitsapp

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.habitsapp.data.AppDatabase
import com.example.habitsapp.data.AppRepository
import com.example.habitsapp.data.HabitRepository

class HabitsAppContainer(context: Context): AppContainer {
    val database: AppDatabase = Room.databaseBuilder<AppDatabase>(context, "app-database")
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .build()

    override val repository: HabitRepository by lazy {
        AppRepository(database.habitDao(), database.habitHistoryEntryDao(), context)
    }
}