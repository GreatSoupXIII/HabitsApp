package com.example.habitsapp

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.habitsapp.data.AppDatabase
import com.example.habitsapp.data.dao.HabitDao
import com.example.habitsapp.data.entities.HabitData
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var habitDao: HabitDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder<AppDatabase>(context)
            .setDriver(BundledSQLiteDriver())
            .build()
        habitDao = db.habitDao()
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
            3,
            true,
            8,
            0
        )
        habitDao.insert(habit)
        val getResponse = habitDao.getAll()
        assertEquals(habit,getResponse.single())
        habitDao.delete(habit)
        val getResponseAfterDelete = habitDao.getAll()
        assertEquals(true, getResponseAfterDelete.isEmpty())
    }

    //the database should be empty after an element is inserted and deleted
    @Test
    fun writeHabitAndDelete() = runTest {
        val habit = HabitData(
            2,
            "Сыграть в Хартстоун",
            3,
            true,
            8,
            0
        )
        habitDao.insert(habit)
        habitDao.delete(habit)
        val getResponseAfterDelete = habitDao.getAll()
        assertEquals(true, getResponseAfterDelete.isEmpty())
    }

    //habit should have a new value after being edited
    @Test
    fun writeHabitAndEdit() = runTest {
        val habit = HabitData(
            3,
            "Сыграть в МтГ",
            3,
            true,
            8,
            0
        )
        habitDao.insert(habit)
        val newHabit = HabitData(
            3,
            "Не играть в МтГ",
            123,
            false,
            8,
            0
        )
        habitDao.update(newHabit)
        val getResponseAfterEdit = habitDao.getAll()
        assertEquals(newHabit, getResponseAfterEdit.single())
    }
}