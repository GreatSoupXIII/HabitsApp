package com.example.habitsapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.Reminder
import com.example.habitsapp.routes.HabitAdd
import com.example.habitsapp.routes.HabitList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HABIT_LIST) }
    val viewModel: AppViewModel = viewModel()

    val focusedHabit: MutableState<Habit?> = remember { mutableStateOf(null) }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text(stringResource(currentDestination.label)) },
            actions = {
                if(currentDestination == AppDestinations.HABIT_LIST) {
                    if(focusedHabit.value == null) {
                        IconButton(onClick = { currentDestination = AppDestinations.HABIT_ADD }) { Text("ADD") }
                        IconButton(onClick = {}) { Text("MOR") }
                    }
                    else {
                        IconButton(onClick = { currentDestination = AppDestinations.HABIT_EDIT }) { Text("EDT") }
                        IconButton(onClick = {}) { Text("DEL") }
                    }
                }
            },
            navigationIcon = {
                if (currentDestination != AppDestinations.HABIT_LIST) {
                    IconButton(onClick = { currentDestination = AppDestinations.HABIT_LIST }) { Text("BCK") }
                }
            }
        ) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when(currentDestination) {
            AppDestinations.HABIT_LIST -> HabitList(
                Modifier.padding(innerPadding),
                viewModel.loadHabitList(),
                focusedHabit,
                onClickHabit = {}
            )
            AppDestinations.HABIT_ADD -> HabitAdd(Modifier.padding(innerPadding)) { habit -> viewModel.addHabitAndReload(habit) }
            AppDestinations.HABIT_EDIT -> Text("TBD", Modifier.padding(innerPadding))
            AppDestinations.HABIT_INFO -> Text("TBD", Modifier.padding(innerPadding))
        }

    }
}

enum class AppDestinations(
    val label: Int
) {
    HABIT_LIST( R.string.title_habit_list),
    HABIT_ADD(R.string.title_habit_add),
    HABIT_EDIT(R.string.title_habit_edit),
    HABIT_INFO(R.string.title_habit_info)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HabitsAppPreview() {

    val habitList: List<Habit> = listOf(
        Habit(1, "Сделать 1 отжимание", 2, Reminder(false, 1, 20)),
        Habit(2, "Сыграть в Гвинт", 999, Reminder(true, 5, 30))
    )

    val focusedHabit: MutableState<Habit?> = remember { mutableStateOf(habitList[0]) }

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HABIT_LIST) }


    Scaffold(
        topBar = { TopAppBar(
            title = { Text(stringResource(currentDestination.label)) },
            actions = {
                if(currentDestination == AppDestinations.HABIT_LIST) {
                    if(focusedHabit.value == null) {
                        IconButton(onClick = { currentDestination = AppDestinations.HABIT_ADD }) { Text("ADD") }
                        IconButton(onClick = {}) { Text("MOR") }
                    }
                    else {
                        IconButton(onClick = { currentDestination = AppDestinations.HABIT_EDIT }) { Text("EDT") }
                        IconButton(onClick = {}) { Text("DEL") }
                    }
                }
            },
        ) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when(currentDestination) {
            AppDestinations.HABIT_LIST -> HabitList(
                Modifier.padding(innerPadding),
                habitList,
                focusedHabit,
                onClickHabit = {}
            )
            AppDestinations.HABIT_ADD -> Text("TBD", Modifier.padding(innerPadding))
            AppDestinations.HABIT_EDIT -> Text("TBD", Modifier.padding(innerPadding))
            AppDestinations.HABIT_INFO -> Text("TBD", Modifier.padding(innerPadding))
        }

    }
}