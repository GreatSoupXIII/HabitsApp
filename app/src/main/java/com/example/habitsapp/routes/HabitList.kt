package com.example.habitsapp.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitsapp.components.HabitListItem
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.Reminder

@Composable
fun HabitList(modifier: Modifier, habitList: List<Habit>) {
    Column(modifier = modifier
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
            HabitListItem(habit)
        }
    }
}

@Composable
@Preview
fun HabitListPreview() {
    //test data
    val habitList: List<Habit> = listOf(
        Habit("Сделать 1 отжимание", 2, null),
        Habit("Сыграть в Гвинт", 999, Reminder(true, 5, 30))
    )

    HabitList(modifier = Modifier, habitList)
}