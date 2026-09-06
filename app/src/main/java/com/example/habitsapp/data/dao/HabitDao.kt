package com.example.habitsapp.data.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.data.entities.HabitHistoryEntryData

@Dao
interface HabitDao {
    @Query("""
        SELECT * FROM habitData
        LEFT JOIN habitHistoryEntryData
            ON habitData.id = habitHistoryEntryData.habitId
        ORDER BY habitData.id ASC, habitHistoryEntryData.date DESC
        """)
    suspend fun getAll(): Map<HabitData, List<HabitHistoryEntryData>>

    @Insert
    suspend fun insert(habit: HabitData)

    @Update
    suspend fun update(habit: HabitData)

    @Query("""
        DELETE FROM habitData
        WHERE id IN (:ids)
        """)
    suspend fun delete(ids: List<Int>)
}