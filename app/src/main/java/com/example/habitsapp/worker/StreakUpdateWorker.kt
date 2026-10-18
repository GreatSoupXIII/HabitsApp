package com.example.habitsapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.habitsapp.R
import com.example.habitsapp.data.AppRepository
import com.example.habitsapp.models.Habit

class StreakUpdateWorker(appContext: Context, params: WorkerParameters )
    : CoroutineWorker(appContext, params) {

    private val context = appContext
    private val repository = AppRepository(appContext)
    val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
    val ADVICE_CHANNEL_ID = "0"
    val name = "test channel name 1 "
    val descriptionText = "test description text 1"

    //an ID for a notification per notification
    //it is reset at each interval
    var notificationId = 0

    override suspend fun doWork(): Result {

        val channel = NotificationChannel(
            ADVICE_CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        channel.description = descriptionText

        notificationManager?.createNotificationChannel(
            channel
        )

        val habitData = repository.loadHabitList()
        val habitsList = mutableListOf<Habit>()

        habitData.map {
            habitsList.add(Habit(it.key, it.value))
        }
        for (habit in habitsList) {
            if(habit.history.getFailureStreak() > 2){
                val builder = NotificationCompat.Builder(applicationContext, ADVICE_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_account_box)
                    .setContentTitle(habit.name)
                    .setContentText(
                        context.resources.getString(R.string.advice_1) +
                                " ${habit.history.getFailureStreak()}" +
                                context.resources.getString(R.string.advice_2)
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                with(NotificationManagerCompat.from(applicationContext)) {
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return@with
                    }
                    notify(notificationId++, builder.build())
                }
            }
        }
        return Result.success()
    }
}