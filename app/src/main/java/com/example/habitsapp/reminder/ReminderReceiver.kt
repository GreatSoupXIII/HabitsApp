package com.example.habitsapp.reminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.habitsapp.R
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        //todo: probably move channel creation to app start
        val NOTIFICATION_CHANNEL_ID = "1"
        val name = "test channel name 2 "
        val descriptionText = "test description text 2"
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = descriptionText
        notificationManager?.createNotificationChannel(
            channel
        )

        val habitId = intent?.getIntExtra("EXTRA_HABIT_ID", -1)
        val habitName = intent?.getStringExtra("EXTRA_HABIT_NAME")
        val habitHour = intent?.getIntExtra("EXTRA_HABIT_HOUR", -1)
        val habitMinute = intent?.getIntExtra("EXTRA_HABIT_MINUTE", -1)
        
        val builder = NotificationCompat.Builder(context!!, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_account_box)
            .setContentTitle(habitName)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(habitId!!, builder.build())
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, habitHour!!)
            set(Calendar.MINUTE, habitMinute!!)
            set(Calendar.SECOND, 0)
            add(Calendar.DAY_OF_MONTH, 1) //to schedule for next day
        }

        val alarmIntent = Intent(context, ReminderReceiver::class.java)
            .putExtra("EXTRA_HABIT_ID", habitId)
            .putExtra("EXTRA_HABIT_NAME", habitName)
            .putExtra("EXTRA_HABIT_HOUR", habitHour)
            .putExtra("EXTRA_HABIT_MINUTE", habitMinute)
            .let { intent ->
                PendingIntent.getBroadcast(context, habitId!!, intent, PendingIntent.FLAG_IMMUTABLE)
            }

        //if app needs permission and doesn't have it, don't do anything
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmMgr.canScheduleExactAlarms()) {
            return
        }

        //otherwise it's good
        alarmMgr.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
    }
}