package com.example.habitsapp.data.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.habitsapp.data.entities.HabitData

@Dao
interface HabitDao {
    @Query("SELECT * FROM habitData")
    suspend fun getAll(): List<HabitData>

    @Insert
    suspend fun insert(habit: HabitData)

    @Update
    suspend fun update(habit: HabitData)

    @Delete
    suspend fun delete(habit: HabitData)
}