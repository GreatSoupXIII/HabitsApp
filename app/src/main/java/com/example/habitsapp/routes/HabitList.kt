package com.example.habitsapp.routes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitsapp.components.HabitListItem
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.Reminder

@Composable
fun HabitList(
    modifier: Modifier,
    habitList: List<Habit>,
    focusedHabit: MutableState<Habit?>,
    onClickHabit: (Habit) -> Unit
) {

    Column(modifier = modifier
        .clickable(
            onClick = {
                if (focusedHabit.value != null) focusedHabit.value = null
            },
            indication = null,
            interactionSource = null
        )
        .fillMaxSize()
        .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Название")
            Text("Серия выполнений")
        }
        for (habit: Habit in habitList) {
            HabitListItem(
                habit,
                habit.id == focusedHabit.value?.id,
                {
                    if(focusedHabit.value != null) focusedHabit.value = null
                    else onClickHabit(habit)
                },
                {focusedHabit.value = habit}
            )
        }
    }
}

@Composable
@Preview
fun HabitListPreview() {
    //test data
    val habitList: List<Habit> = listOf(
        Habit(1, "Сделать 1 отжимание", 2, Reminder(false, 5, 30)),
        Habit(2, "Сыграть в Гвинт", 999, Reminder(true, 5, 30))
    )

    val focusedHabit: MutableState<Habit?> = remember { mutableStateOf(habitList[0]) }

    HabitList(modifier = Modifier, habitList, focusedHabit, {})
}