package com.example.habitsapp.data.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import com.example.habitsapp.data.entities.HabitHistoryEntryData

@Dao
interface HabitHistoryEntryDao {
    @Insert
    suspend fun insert(entry: HabitHistoryEntryData)

    @Delete
    suspend fun delete(entry: HabitHistoryEntryData)
}