package com.example.habitsapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.habitsapp.models.Habit
import com.example.habitsapp.routes.HabitAdd
import com.example.habitsapp.routes.HabitEdit
import com.example.habitsapp.routes.HabitList
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsApp() {
    val viewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val focusedHabits = remember { mutableStateListOf<Habit>() }
    val showDeleteDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text(
                when {
                    backStackEntry?.destination?.hasRoute<HabitListDestination>() == true -> stringResource(R.string.title_habit_list)
                    backStackEntry?.destination?.hasRoute<HabitAddDestination>() == true -> stringResource(R.string.title_habit_add)
                    backStackEntry?.destination?.hasRoute<HabitEditDestination>() == true -> stringResource(R.string.title_habit_edit)
                    backStackEntry?.destination?.hasRoute<HabitInfoDestination>() == true -> stringResource(R.string.title_habit_info)
                    else -> stringResource(R.string.title_habit_list) //a crutch at app start
                }
            ) },
            actions = {
                if(backStackEntry?.destination?.hasRoute<HabitListDestination>() == true) {
                    if(focusedHabits.isEmpty()) {
                        IconButton(onClick = { navController.navigate(HabitAddDestination) }) { Icon(
                            painterResource(R.drawable.add_24px),
                            "Add a habit"
                        ) }
                        IconButton(onClick = {}) { Icon(
                            painterResource(R.drawable.more_vert_24px),
                            "More options"
                        ) }
                    }
                    else if (focusedHabits.size == 1) {
                        IconButton(onClick = {
                            navController.navigate(HabitEditDestination(focusedHabits[0].id!!))
                        }) { Icon(
                            painterResource(R.drawable.edit_24px),
                            "Edit a habit"
                        ) }
                        IconButton(onClick = {
                            showDeleteDialog.value = true
                        }) { Icon(
                            painterResource(R.drawable.delete_24px),
                            "Delete selected habits"
                        ) }
                    }
                    else {
                        IconButton(onClick = {
                            showDeleteDialog.value = true
                        }) { Icon(
                            painterResource(R.drawable.delete_24px),
                            "Delete selected habits"
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
                if (backStackEntry?.destination?.hasRoute<HabitListDestination>() == false) {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(
                        painterResource(R.drawable.arrow_back_24px),
                        "Go back"
                    ) }
                }
            }
        ) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(navController, HabitListDestination) {
            composable<HabitListDestination> {
                HabitList(
                    Modifier.padding(innerPadding),
                    viewModel.loadHabitList(),
                    focusedHabits,
                    onClickHabit = { navController.navigate(HabitInfoDestination(it.id!!)) },
                    onMark = { habitHistoryEntry -> viewModel.addEntry(habitHistoryEntry) },
                    onUnmark = { habitHistoryEntry -> viewModel.deleteEntry(habitHistoryEntry) }
                )
            }
            composable<HabitAddDestination> {
                HabitAdd(Modifier.padding(innerPadding)) { habit ->
                    viewModel.addHabitAndReload(habit)
                    navController.popBackStack()
                }
            }
            composable<HabitEditDestination> { backStackEntry ->
                val habitEditDestination: HabitEditDestination = backStackEntry.toRoute()
                val habit = viewModel.habitsList.find { it.id == habitEditDestination.habitId }
                HabitEdit(Modifier.padding(innerPadding), habit!!) { habit ->
                    viewModel.updateHabitAndReload(habit)
                    navController.popBackStack()
                    focusedHabits.clear()
                }
            }
            composable<HabitInfoDestination> { backStackEntry ->
                val habitInfoDestination: HabitInfoDestination = backStackEntry.toRoute()
                val habit = viewModel.habitsList.find { it.id == habitInfoDestination.habitId }
                //this is a test component, must be replaced by a real screen
                Column(Modifier.padding(innerPadding)){
                    if(habit == null) Text("ERROR: Habit not found")
                    else Text(habit.name, color = Color.White)
                }
            }
        }
        when {
            showDeleteDialog.value -> {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog.value = false
                    },
                    title = {
                        if(focusedHabits.size > 1) Text(text = stringResource(R.string.delete_dialog_title_plural))
                        else Text(text = stringResource(R.string.delete_dialog_title_single))
                    },
                    text = { Text(text = stringResource(R.string.delete_dialog_text)) },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog.value = false }) { Text(stringResource(R.string.no)) }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteHabitsAndReload(focusedHabits.toList())
                            focusedHabits.clear()
                            showDeleteDialog.value = false
                        }) { Text(stringResource(R.string.yes)) }
                    }
                )
            }
        }
    }
}

@Serializable
object HabitListDestination
@Serializable
object HabitAddDestination
@Serializable
data class HabitEditDestination(val habitId: Int)
@Serializable
data class HabitInfoDestination(val habitId: Int)