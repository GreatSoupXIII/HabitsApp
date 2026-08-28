package com.example.habitsapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitsapp.R
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.HabitHistory
import com.example.habitsapp.models.HabitHistoryEntry
import com.example.habitsapp.models.Reminder
import java.time.LocalDate

@Composable
fun HabitListItem(
    habit: Habit,
    focused: Boolean,
    onMark: () -> Unit,
    onUnmark: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {

    val marked = !habit.history.items.isEmpty() &&
        habit.history.items[0].date == LocalDate.now()

    Row(
        modifier = Modifier
            .background(color = if(focused) MaterialTheme.colorScheme.primary else Color(0x60888888))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(habit.name)
            Text(
                text =
                    "Серия выполнений: " +
                    habit.history.getSuccessStreak().toString(),
                color = Color.Unspecified.copy(alpha = 0.7f)
            )
        }
        IconButton(
            onClick = if(marked) onUnmark else onMark
        )
        {
            if(marked) IconButton(onUnmark) {
                Icon(
                    painterResource(R.drawable.check_box_24px),
                    "Unmark this habit",
                    Modifier.size(32.dp)
                )
            }
            else IconButton(onMark) {
                Icon(
                    painterResource(R.drawable.check_box_outline_blank_24px),
                    "Mark this habit",
                    Modifier.size(32.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun HabitListItemPreview() {
    val habit = Habit(1, "Сделать 1 отжимание", HabitHistory(), Reminder(true, 5, 30))
    HabitListItem(habit, false,  {}, {}, {}, {})
}

@Preview
@Composable
fun HabitListItemMarkedPreview() {
    val habit = Habit(1, "Сделать 1 отжимание", HabitHistory(listOf(HabitHistoryEntry(1, 1))), Reminder(true, 5, 30))
    HabitListItem(habit, false,  {}, {}, {}, {})
}

@Preview
@Composable
fun HabitListItemFocusedPreview() {
    val habit = Habit(1, "Сделать 1 отжимание", HabitHistory(), Reminder(true, 5, 30))
    HabitListItem(habit, true, {}, {}, {}, {})
}