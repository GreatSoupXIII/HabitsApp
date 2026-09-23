package com.example.habitsapp

import android.app.Application

class MainApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate(){
        super.onCreate()
        container = HabitsAppContainer(this)
    }
}