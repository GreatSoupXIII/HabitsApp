package com.example.habitsapp.data

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.habitsapp.data.dao.HabitDao
import com.example.habitsapp.data.entities.HabitData

@Database(entities = [HabitData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}