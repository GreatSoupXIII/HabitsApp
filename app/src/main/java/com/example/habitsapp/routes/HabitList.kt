package com.example.habitsapp.routes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitsapp.components.HabitListItem
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.HabitHistory
import com.example.habitsapp.models.HabitHistoryEntry
import com.example.habitsapp.models.Reminder
import java.time.LocalDate

@Composable
fun HabitList(
    modifier: Modifier,
    habitList: List<Habit>,
    focusedHabits: SnapshotStateList<Habit>,
    onClickHabit: (Habit) -> Unit,
    onMark: (HabitHistoryEntry) -> Unit,
    onUnmark: (HabitHistoryEntry) -> Unit
) {

    Column(modifier = modifier
        .clickable(
            onClick = {
                if (!focusedHabits.isEmpty()) focusedHabits.clear()
            },
            indication = null,
            interactionSource = null
        )
        .fillMaxSize()
        .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (habit: Habit in habitList) {
            HabitListItem(
                habit,
                focusedHabits.contains(habit),
                { onMark(HabitHistoryEntry(habitId = habit.id!!)) },
                { onUnmark(habit.history.items[0]) }, //you can only unmark if there is an entry for today, so items[0] is OK
                {
                    if(focusedHabits.isEmpty()) onClickHabit(habit)
                    else if(!focusedHabits.contains(habit)) focusedHabits.add(habit)
                    else focusedHabits.remove(habit)

                },
                {
                    if(focusedHabits.isEmpty()) focusedHabits.add(habit)

                }
            )
        }
    }
}

@Composable
@Preview
fun HabitListPreview() {
    //test data
    val habitList: List<Habit> = listOf(
        Habit(1, "Сделать 1 приседание", HabitHistory(listOf(HabitHistoryEntry(1, 1))), Reminder(true, 5, 30)),
        Habit(2, "Сделать 1 отжимание", HabitHistory(listOf(HabitHistoryEntry(1, 2, LocalDate.of(1990, 1, 1)))), Reminder(false, 5, 30)),
        Habit(3, "Сыграть в Гвинт", HabitHistory(), Reminder(true, 5, 30))
    )

    val focusedHabits = remember { mutableStateListOf(habitList[0], habitList[2]) }

    HabitList(modifier = Modifier, habitList, focusedHabits, {}, {}, {})
}