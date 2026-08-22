package com.example.habitsapp.data

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.habitsapp.data.dao.HabitDao
import com.example.habitsapp.data.dao.HabitHistoryEntryDao
import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.data.entities.HabitHistoryEntryData

@Database(entities = [HabitData::class, HabitHistoryEntryData::class],  version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitHistoryEntryDao(): HabitHistoryEntryDao
}