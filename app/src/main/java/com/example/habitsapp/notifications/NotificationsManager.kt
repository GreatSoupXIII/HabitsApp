package com.example.habitsapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class NotificationsManager {
    val ADVICE_CHANNEL_ID = "0"
    val NOTIFICATION_CHANNEL_ID = "1"

    private var nextAdviceId = 0
    private var nextNotificationId = 0

    fun createAdviceChannel(context: Context) {
        // todo: make it translatable like below
        // val name = context.getString(R.string.channel_name)
        val name = "test channel name 1 "
        val descriptionText = "test description text 1"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(ADVICE_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system.
        val notificationManager: NotificationManager =
            context.getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotificationChannel(context: Context) {
        // todo: make it translatable like below
        // val name = context.getString(R.string.channel_name)
        val name = "test channel name 2"
        val descriptionText = "test description text 2"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system.
        val notificationManager: NotificationManager =
            context.getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun getNewAdviceId(): Int {
        return nextAdviceId++
    }

    fun getNewNotificationId(): Int {
        return nextNotificationId++
    }
}