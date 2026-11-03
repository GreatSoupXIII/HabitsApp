package com.example.habitsapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DateChangeReceiver(val onDateChange: () -> Unit): BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        onDateChange()
    }

}