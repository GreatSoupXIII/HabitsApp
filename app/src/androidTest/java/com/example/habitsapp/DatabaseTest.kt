package com.example.habitsapp

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.habitsapp.data.AppDatabase
import com.example.habitsapp.data.dao.HabitDao
import com.example.habitsapp.data.dao.HabitHistoryEntryDao
import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.data.entities.HabitHistoryEntryData
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var habitDao: HabitDao
    private lateinit var habitHistoryEntryDao: HabitHistoryEntryDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder<AppDatabase>(context)
            .setDriver(BundledSQLiteDriver())
            .build()
        habitDao = db.habitDao()
        habitHistoryEntryDao = db.habitHistoryEntryDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    //habit should stay in database after being inserted
    @Test
    fun writeHabitAndReadInList() = runTest {
        val habit = HabitData(
            1,
                "Сыграть в Гвинт",
            true,
            8,
            0,
            "1970-01-01"
        )
        habitDao.insert(habit)
        val getResponse = habitDao.getAll()
        assertEquals(
            mapOf(Pair(habit, listOf<HabitHistoryEntryData>())),
            getResponse
        )
        habitDao.delete(listOf(habit.id!!))
        val getResponseAfterDelete = habitDao.getAll()
        assertEquals(true, getResponseAfterDelete.isEmpty())
    }

    //the database should be empty after an element is inserted and deleted
    @Test
    fun writeHabitAndDelete() = runTest {
        val habit = HabitData(
            2,
            "Сыграть в Хартстоун",
            true,
            8,
            0,
            "1970-01-01"
        )
        habitDao.insert(habit)
        habitDao.delete(listOf(habit.id!!))
        val getResponseAfterDelete = habitDao.getAll()
        assertEquals(true, getResponseAfterDelete.isEmpty())
    }

    //habit should have a new value after being edited
    @Test
    fun writeHabitAndEdit() = runTest {
        val habit = HabitData(
            3,
            "Сыграть в МтГ",
            true,
            8,
            0,
            "1970-01-01"
        )
        habitDao.insert(habit)
        val newHabit = HabitData(
            3,
            "Не играть в МтГ",
            false,
            8,
            0,
            "1970-01-01"
        )
        habitDao.update(newHabit)
        val getResponseAfterEdit = habitDao.getAll()

        assertEquals(
            mapOf(Pair(newHabit, listOf<HabitHistoryEntryData>())),
            getResponseAfterEdit
        )
    }

    //it should be possible to delete all history entries
    //with the same habitId
    @Test
    fun writeEntriesAndDeleteByHabitId() = runTest {
        val habit = HabitData(
            3,
            "Не играть в МтГ",
            false,
            8,
            0,
            "1970-01-01"
        )

        val items = listOf(
            HabitHistoryEntryData(3, 3, "2026-08-20"),
            HabitHistoryEntryData(2, 3, "2026-08-19"),
            HabitHistoryEntryData(1, 3, "2026-08-18"),
        )

        habitDao.insert(habit)
        for(item: HabitHistoryEntryData in items) {
            habitHistoryEntryDao.insert(item)
        }

        habitHistoryEntryDao.deleteByHabitIds(listOf(habit.id!!))

        val getResponseAfterEdit = habitDao.getAll()

        assertEquals(
            mapOf(Pair(habit, listOf<HabitHistoryEntryData>())),
            getResponseAfterEdit
        )
    }

    //the database should be empty after multiple habits are inserted and deleted
    @Test
    fun writeMultipleHabitsAndDelete() = runTest {
        val habit1 = HabitData(
            2,
            "Сыграть в Хартстоун",
            true,
            8,
            0,
            "1970-01-01"
        )
        val habit2 = HabitData(
            3,
            "Сыграть в Гвинт",
            true,
            8,
            0,
            "1970-01-01"
        )
        habitDao.insert(habit1)
        habitDao.insert(habit2)
        habitDao.delete(listOf(habit1.id!!, habit2.id!!))
        val getResponseAfterDelete = habitDao.getAll()
        assertEquals(true, getResponseAfterDelete.isEmpty())
    }

    //it should be possible to delete history entries
    //with the same habitId for multiple habits
    @Test
    fun writeEntriesAndDeleteByMultipleHabitIds() = runTest {
        val habit1 = HabitData(
            2,
            "Не играть в МтГ",
            false,
            8,
            0,
            "1970-01-01"
        )

        val habit2 = HabitData(
            3,
            "Играть в МтГ",
            false,
            8,
            0,
            "1970-01-01"
        )

        val items = listOf(
            HabitHistoryEntryData(3, 2, "2026-08-20"),
            HabitHistoryEntryData(2, 2, "2026-08-19"),
            HabitHistoryEntryData(1, 2, "2026-08-18"),
            HabitHistoryEntryData(6, 3, "2026-08-20"),
            HabitHistoryEntryData(5, 3, "2026-08-19"),
            HabitHistoryEntryData(4, 3, "2026-08-18"),
        )

        habitDao.insert(habit1)
        habitDao.insert(habit2)

        for(item: HabitHistoryEntryData in items) {
            habitHistoryEntryDao.insert(item)
        }

        habitHistoryEntryDao.deleteByHabitIds(listOf(habit1.id!!, habit2.id!!))

        val getResponseAfterEdit = habitDao.getAll()

        assertEquals(
            mapOf(Pair(habit1, listOf<HabitHistoryEntryData>()), Pair(habit2, listOf())),
            getResponseAfterEdit
        )
    }
}