package com.example.habitsapp.models

import java.time.LocalDate

data class HabitHistoryEntry (
    val id: Int? = null,
    val date: LocalDate = LocalDate.now()
)