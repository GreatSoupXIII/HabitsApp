package com.example.habitsapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.HabitHistory
import com.example.habitsapp.models.HabitHistoryEntry
import com.example.habitsapp.models.Reminder
import com.example.habitsapp.routes.HabitAdd
import com.example.habitsapp.routes.HabitEdit
import com.example.habitsapp.routes.HabitList
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HABIT_LIST) }
    val viewModel: AppViewModel = viewModel()

    val focusedHabits = remember { mutableStateListOf<Habit>() }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text(stringResource(currentDestination.label)) },
            actions = {
                if(currentDestination == AppDestinations.HABIT_LIST) {
                    if(focusedHabits.isEmpty()) {
                        IconButton(onClick = { currentDestination = AppDestinations.HABIT_ADD }) { Icon(
                            painterResource(R.drawable.add_24px),
                            "Add a habit"
                        ) }
                        IconButton(onClick = {}) { Icon(
                            painterResource(R.drawable.more_vert_24px),
                            "More options"
                        ) }
                    }
                    else if (focusedHabits.size == 1) {
                        IconButton(onClick = { currentDestination = AppDestinations.HABIT_EDIT }) { Icon(
                            painterResource(R.drawable.edit_24px),
                            "Edit a habit"
                        ) }
                        IconButton(onClick = {
                            viewModel.deleteHabitsAndReload(focusedHabits.toList())
                            focusedHabits.clear()
                        }) { Icon(
                            painterResource(R.drawable.delete_24px),
                            "Delete a habit"
                        ) }
                    }
                    else {
                        IconButton(onClick = {
                            viewModel.deleteHabitsAndReload(focusedHabits.toList())
                            focusedHabits.clear()
                        }) { Icon(
                            painterResource(R.drawable.delete_24px),
                            "Delete a habit"
                        ) }
                    }
                }
            },
            colors = TopAppBarColors(
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.onSurface
            ),
            navigationIcon = {
                if (currentDestination != AppDestinations.HABIT_LIST) {
                    IconButton(onClick = { currentDestination = AppDestinations.HABIT_LIST }) { Icon(
                        painterResource(R.drawable.arrow_back_24px),
                        "Go back"
                    ) }
                }
            }
        ) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when(currentDestination) {
            AppDestinations.HABIT_LIST -> HabitList(
                Modifier.padding(innerPadding),
                viewModel.loadHabitList(),
                focusedHabits,
                onClickHabit = {},
                onMark = { habitHistoryEntry -> viewModel.addEntry(habitHistoryEntry) },
                onUnmark = { habitHistoryEntry -> viewModel.deleteEntry(habitHistoryEntry) }
            )
            AppDestinations.HABIT_ADD -> HabitAdd(Modifier.padding(innerPadding)) {
                habit -> viewModel.addHabitAndReload(habit)
                currentDestination = AppDestinations.HABIT_LIST
            }
            AppDestinations.HABIT_EDIT -> HabitEdit(Modifier.padding(innerPadding), focusedHabits[0]) {
                habit -> viewModel.updateHabitAndReload(habit)
                currentDestination = AppDestinations.HABIT_LIST
                focusedHabits.clear()
            }
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
fun HabitsAppFocusedOnePreview() {

    val habitList: List<Habit> = listOf(
        Habit(1, "Сделать 1 приседание", HabitHistory(listOf(HabitHistoryEntry(1, 1))), Reminder(true, 5, 30)),
        Habit(2, "Сделать 1 отжимание", HabitHistory(listOf(HabitHistoryEntry(1, 2, LocalDate.of(1990, 1, 1)))), Reminder(false, 5, 30)),
        Habit(3, "Сыграть в Гвинт", HabitHistory(), Reminder(true, 5, 30))
    )

    val focusedHabits = remember { mutableStateListOf<Habit>(habitList[0]) }

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HABIT_LIST) }


    Scaffold(
        topBar = { TopAppBar(
            title = { Text(stringResource(currentDestination.label)) },
            actions = {
                if(currentDestination == AppDestinations.HABIT_LIST) {
                    if(!focusedHabits.isEmpty()) {
                        IconButton(onClick = {}) { Icon(
                            painterResource(R.drawable.add_24px),
                            "Add a habit"
                        ) }
                        IconButton(onClick = {}) { Icon(
                            painterResource(R.drawable.more_vert_24px),
                            "More options"
                        ) }
                    }
                    else {
                        IconButton(onClick = {}) { Icon(
                            painterResource(R.drawable.edit_24px),
                            "Edit a habit"
                        ) }
                        IconButton(onClick = {}) { Icon(
                            painterResource(R.drawable.delete_24px),
                            "Delete a habit"
                        ) }
                    }
                }
            },
            colors = TopAppBarColors(
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.onSurface
            )
        ) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when(currentDestination) {
            AppDestinations.HABIT_LIST -> HabitList(
                Modifier.padding(innerPadding),
                habitList,
                focusedHabits,
                onClickHabit = {},
                onMark = {},
                onUnmark = {}
            )
            AppDestinations.HABIT_ADD -> Text("TBD", Modifier.padding(innerPadding))
            AppDestinations.HABIT_EDIT -> Text("TBD", Modifier.padding(innerPadding))
            AppDestinations.HABIT_INFO -> Text("TBD", Modifier.padding(innerPadding))
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HabitsAppPreviewFocusedMany() {

    val habitList: List<Habit> = listOf(
        Habit(1, "Сделать 1 приседание", HabitHistory(listOf(HabitHistoryEntry(1, 1))), Reminder(true, 5, 30)),
        Habit(2, "Сделать 1 отжимание", HabitHistory(listOf(HabitHistoryEntry(1, 2, LocalDate.of(1990, 1, 1)))), Reminder(false, 5, 30)),
        Habit(3, "Сыграть в Гвинт", HabitHistory(), Reminder(true, 5, 30))
    )

    val focusedHabits = remember { mutableStateListOf(habitList[0], habitList[2]) }

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HABIT_LIST) }


    Scaffold(
        topBar = { TopAppBar(
            title = { Text(stringResource(currentDestination.label)) },
            actions = {
                if(currentDestination == AppDestinations.HABIT_LIST) {
                    if(!focusedHabits.isEmpty()) {
                        IconButton(onClick = {}) { Icon(
                            painterResource(R.drawable.add_24px),
                            "Add a habit"
                        ) }
                        IconButton(onClick = {}) { Icon(
                            painterResource(R.drawable.more_vert_24px),
                            "More options"
                        ) }
                    }
                    else {
                        IconButton(onClick = {}) { Icon(
                            painterResource(R.drawable.edit_24px),
                            "Edit a habit"
                        ) }
                        IconButton(onClick = {}) { Icon(
                            painterResource(R.drawable.delete_24px),
                            "Delete a habit"
                        ) }
                    }
                }
            },
            colors = TopAppBarColors(
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.onSurface
            )
        ) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when(currentDestination) {
            AppDestinations.HABIT_LIST -> HabitList(
                Modifier.padding(innerPadding),
                habitList,
                focusedHabits,
                onClickHabit = {},
                onMark = {},
                onUnmark = {}
            )
            AppDestinations.HABIT_ADD -> Text("TBD", Modifier.padding(innerPadding))
            AppDestinations.HABIT_EDIT -> Text("TBD", Modifier.padding(innerPadding))
            AppDestinations.HABIT_INFO -> Text("TBD", Modifier.padding(innerPadding))
        }

    }
}