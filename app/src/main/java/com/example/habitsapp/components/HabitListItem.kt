package com.example.habitsapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.Reminder

@Composable
fun HabitListItem(habit: Habit) {
    Row(
        modifier = Modifier
            .background(color = Color(213, 242, 255))
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(habit.name)
        Text(habit.successStreak.toString())
    }
}

@Preview
@Composable
fun HabitListItemPreview() {
    val habit = Habit("Сделать 1 отжимание", 2, Reminder(true, 5, 30))
    HabitListItem(habit)
}