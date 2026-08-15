package com.example.habitsapp.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitsapp.R
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.Reminder

@Composable
fun HabitAdd(
    modifier: Modifier,
    onAddHabit: (habit: Habit) -> Unit
) {
    val habitName: MutableState<String> = remember{ mutableStateOf("") }
    val isReminderActive: MutableState<Boolean> = remember{ mutableStateOf(false) }
    val reminderHours: MutableState<Int> = remember{ mutableIntStateOf(8) }
    val reminderMinutes: MutableState<Int> = remember{ mutableIntStateOf(0) }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.add_habit_tip))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = habitName.value,
            onValueChange = { value: String -> habitName.value = value },
            label = { Text("Название") },
            placeholder = { Text("Сделать 1 отжимание") },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Напоминание")
            Switch(checked = isReminderActive.value, onCheckedChange = {isReminderActive.value = it})
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = reminderHours.value.toString(),
            onValueChange = { value: String ->
                if(value == "") reminderHours.value = 0
                else if(value.toInt() > 59) reminderHours.value = 23
                else reminderHours.value = value.toInt()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Часы") },
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = reminderMinutes.value.toString(),
            onValueChange = { value: String ->
                if (value == "") reminderMinutes.value = 0
                else if(value.toInt() > 59) reminderMinutes.value = 59
                else reminderMinutes.value = value.toInt()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Минуты") },
        )
        Button(onClick = { onAddHabit(
            Habit(
                habitName.value,
                0,
                Reminder(
                    isReminderActive.value,
                    reminderHours.value,
                    reminderMinutes.value
                ))
        ) } ) {
            Text("Создать")
        }
    }
}

@Preview
@Composable
fun HabitAddPreview() {
    HabitAdd(Modifier) {}
}