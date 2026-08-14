package com.example.habitsapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.Reminder
import com.example.habitsapp.routes.HabitList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HABIT_LIST) }
    val viewModel: AppViewModel = viewModel()

    Scaffold(
        topBar = { TopAppBar(
            title = { Text(stringResource(currentDestination.label)) },
            actions = {
                if(currentDestination == AppDestinations.HABIT_LIST) {
                    IconButton(onClick = { currentDestination = AppDestinations.HABIT_ADD }) { Text("ADD") }
                    IconButton(onClick = {}) { Text("MOR") }
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
            AppDestinations.HABIT_LIST -> HabitList(Modifier.padding(innerPadding), viewModel.loadHabitList())
            AppDestinations.HABIT_ADD -> Text("TBD", Modifier.padding(innerPadding))
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
        Habit("Сделать 1 отжимание", 2, null),
        Habit("Сыграть в Гвинт", 999, Reminder(true, 5, 30))
    )

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HABIT_LIST) }


    Scaffold(
        topBar = { TopAppBar(
            title = { Text(stringResource(currentDestination.label)) },
            actions = {
                if(currentDestination == AppDestinations.HABIT_LIST) {
                    IconButton(onClick = { currentDestination = AppDestinations.HABIT_ADD }) { Text("ADD") }
                    IconButton(onClick = {}) { Text("MOR") }
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
            AppDestinations.HABIT_LIST -> HabitList(Modifier.padding(innerPadding), habitList)
            AppDestinations.HABIT_ADD -> Text("TBD", Modifier.padding(innerPadding))
            AppDestinations.HABIT_EDIT -> Text("TBD", Modifier.padding(innerPadding))
            AppDestinations.HABIT_INFO -> Text("TBD", Modifier.padding(innerPadding))
        }

    }
}