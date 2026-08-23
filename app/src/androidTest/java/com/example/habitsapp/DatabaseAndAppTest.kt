package com.example.habitsapp

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.habitsapp.data.AppDatabase
import com.example.habitsapp.data.dao.HabitDao
import com.example.habitsapp.data.dao.HabitHistoryEntryDao
import com.example.habitsapp.data.entities.HabitData
import com.example.habitsapp.data.entities.HabitHistoryEntryData
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.HabitHistory
import com.example.habitsapp.models.HabitHistoryEntry
import com.example.habitsapp.models.Reminder
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class DatabaseAndAppTest {
    private lateinit var habitDao: HabitDao
    private lateinit var habitHistoryEntryDao: HabitHistoryEntryDao
    private lateinit var db: AppDatabase

    val habitsList = mutableStateListOf<Habit>()

    val habit = Habit(
        1,
        "Написать 50 слов",
        HabitHistory(),
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
        habitHistoryEntryDao = db.habitHistoryEntryDao()
    }

    private fun loadHabitList(): List<Habit> {
        runTest {
            val habitDataList = habitDao.getAll()

            habitsList.clear()
            //translate all HabitData objects from the database
            //to format used by the application
            habitDataList.map {
                habitsList.add(Habit(it.key, it.value))
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

    private fun addEntry(entry: HabitHistoryEntry) {
        runTest {
            habitHistoryEntryDao.insert(HabitHistoryEntryData(entry))
            loadHabitList()
        }
    }

    private fun deleteEntry(entry: HabitHistoryEntry) {
        runTest {
            habitHistoryEntryDao.delete(HabitHistoryEntryData(entry))
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

    //after adding several non-sorted entries
    //viewModel should have them sorted
    //and put them into a habit object
    @Test
    fun addHabitAndEntries() {
        val expected = Habit(
            1,
            "Написать 50 слов",
            HabitHistory(
                items = listOf(
                    HabitHistoryEntry(3, 1, LocalDate.of(2026, 8, 20)),
                    HabitHistoryEntry(2, 1, LocalDate.of(2026, 8, 19)),
                    HabitHistoryEntry(1, 1, LocalDate.of(2026, 8, 16)),
                )
            ),
            Reminder(
                false,
                19,
                10
            )
        )

        val items = listOf<HabitHistoryEntry>(
            HabitHistoryEntry(1, 1, LocalDate.of(2026, 8, 16)),
            HabitHistoryEntry(2, 1, LocalDate.of(2026, 8, 19)),
            HabitHistoryEntry(3, 1, LocalDate.of(2026, 8, 20)),
        )

        for(item: HabitHistoryEntry in items) {
            addEntry(item)
        }

        addHabitAndReload(habit)

        assertEquals(expected, habitsList[0])
    }

    //after adding several non-sorted entries
    //for multiple habits
    //viewModel should have them sorted
    //and put them into habit objects, which
    //should also be sorted
    @Test
    fun addMultipleHabitsAndEntries() {
        val expected = listOf<Habit>(
            Habit(
                1,
                "Написать 50 слов",
                HabitHistory(
                    items = listOf(
                        HabitHistoryEntry(6, 1, LocalDate.of(2026, 8, 20)),
                        HabitHistoryEntry(9, 1, LocalDate.of(2026, 8, 19)),
                        HabitHistoryEntry(4, 1, LocalDate.of(2026, 8, 16)),
                    )
                ),
                Reminder(
                    false,
                    19,
                    10
                )
            ),
            Habit(
                2,
                "Сделать 1 отжимание",
                HabitHistory(
                    items = listOf(
                        HabitHistoryEntry(3, 2, LocalDate.of(2026, 8, 16)),
                        HabitHistoryEntry(7, 2, LocalDate.of(2026, 8, 13)),
                        HabitHistoryEntry(1, 2, LocalDate.of(2025, 8, 18)),
                    )
                ),
                Reminder(
                    false,
                    19,
                    10
                )
            )
        )
        //adding two habits with no entries
        addHabitAndReload(
            Habit(
                1,
                "Написать 50 слов",
                HabitHistory(),
                Reminder(
                    false,
                    19,
                    10
                )
            )

        )
        addHabitAndReload(
            Habit(
                2,
                "Сделать 1 отжимание",
                HabitHistory(),
                Reminder(
                    false,
                    19,
                    10
                )
            )
        )

        //adding entries for the two habits without any order
        val items = listOf<HabitHistoryEntry>(
            HabitHistoryEntry(1, 2, LocalDate.of(2025, 8, 18)),
            HabitHistoryEntry(9, 1, LocalDate.of(2026, 8, 19)),
            HabitHistoryEntry(3, 2, LocalDate.of(2026, 8, 16)),
            HabitHistoryEntry(7, 2, LocalDate.of(2026, 8, 13)),
            HabitHistoryEntry(4, 1, LocalDate.of(2026, 8, 16)),
            HabitHistoryEntry(6, 1, LocalDate.of(2026, 8, 20)),
        )

        for(item: HabitHistoryEntry in items) {
            addEntry(item)
        }
        assertEquals(expected, habitsList.toList())
    }

    //after deleting an entry and getting
    //the habit the entry should not be there
    @Test
    fun andHabitAndDeleteOneEntry() {
        val expected = Habit(
            1,
            "Написать 50 слов",
            HabitHistory(
                items = listOf(
                    HabitHistoryEntry(3, 1, LocalDate.of(2026, 8, 20)),
                    HabitHistoryEntry(1, 1, LocalDate.of(2026, 8, 16)),
                )
            ),
            Reminder(
                false,
                19,
                10
            )
        )

        val items = listOf<HabitHistoryEntry>(
            HabitHistoryEntry(1, 1, LocalDate.of(2026, 8, 16)),
            HabitHistoryEntry(2, 1, LocalDate.of(2026, 8, 19)),
            HabitHistoryEntry(3, 1, LocalDate.of(2026, 8, 20)),
        )

        addHabitAndReload(habit)

        for(item: HabitHistoryEntry in items) {
            addEntry(item)
        }

        deleteEntry(HabitHistoryEntry(2, 1, LocalDate.of(2026, 8, 19)))

        assertEquals(expected, habitsList[0])
    }


    @After
    fun closeDb() {
        db.close()
    }


}