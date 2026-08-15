package com.example.habitsapp

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.habitsapp.data.AppDatabase
import com.example.habitsapp.data.dao.HabitDao
import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.Reminder
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseAndAppTest {
    private lateinit var habitDao: HabitDao
    private lateinit var db: AppDatabase

    val habitsList = mutableStateListOf<Habit>()

    val habit = Habit(
        "Написать 50 слов",
        122,
        Reminder(
            false,
            19,
            10
        )
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder<AppDatabase>(context)
            .setDriver(BundledSQLiteDriver())
            .build()
        habitDao = db.habitDao()
    }

    private fun loadHabitList(): List<Habit> {
        runTest {
            val habitDataList = habitDao.getAll()

            habitsList.clear()
            //translate all HabitData objects from the database
            //to format used by the application
            for(habitData: HabitData in habitDataList) {
                habitsList.add(Habit(habitData))
            }
        }
        return habitsList
    }

    private fun addHabitAndReload(habit: Habit) {
        runTest {
            habitDao.insert(HabitData(habit))
            loadHabitList()
        }
    }

    //after viewModel makes a call to add a habit to database,
    //viewModel must be able to load that habit into the list
    @Test
    fun addHabitAndLoadIt() {
        addHabitAndReload(habit)
        assertEquals(habit, habitsList[0])
    }

    @After
    fun closeDb() {
        db.close()
    }


}