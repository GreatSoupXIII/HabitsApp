package com.example.habitsapp.models

data class Habit (
    var name: String,
    var successStreak: Int,
    var reminder: Reminder?
)