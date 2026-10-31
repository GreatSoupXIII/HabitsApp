package com.example.habitsapp.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.habitsapp.data.AppRepository
import com.example.habitsapp.data.HabitRepository
import com.example.habitsapp.models.Habit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ReminderRescheduleReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO) //todo: check if SupervisorJob is needed

    override fun onReceive(context: Context, intent: Intent) {

        val pendingResult = goAsync() //todo: what does this shit even do

        if (
            intent.action == "android.intent.action.BOOT_COMPLETED" ||
            intent.action == "android.intent.action.TIMEZONE_CHANGED" ||
            intent.action == "android.intent.action.TIMEZONE_OFFSET_CHANGED" ||
            intent.action == "android.intent.action.TIME_SET" ||
            intent.action == "android.intent.action.DATE_CHANGED"
            ) {
            scope.launch {
                try {
                    val repository: HabitRepository = AppRepository(context)
                    val habitDataList = repository.loadOnlyHabits()
                    for(habitData in habitDataList) {
                        if(habitData.isReminderActive) {
                            repository.createReminder(
                                Habit(
                                    habitData, listOf() //history entries are not used here so leave the list empty
                                )
                            )
                        }
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}