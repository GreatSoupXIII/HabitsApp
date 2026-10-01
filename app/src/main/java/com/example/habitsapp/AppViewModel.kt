package com.example.habitsapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.habitsapp.data.HabitRepository
import com.example.habitsapp.models.Habit
import com.example.habitsapp.models.HabitHistoryEntry
import com.example.habitsapp.notifications.NotificationsManager
import kotlinx.coroutines.launch

class AppViewModel(private val repository: HabitRepository): ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val repository = application.container.repository
                AppViewModel(repository = repository)
            }
        }
    }


//    private val applicationContext = getApplication<Application>().applicationContext
//    val database: AppDatabase = Room.databaseBuilder<AppDatabase>(applicationContext, "app-database")
//        .fallbackToDestructiveMigration(true)
//        .setDriver(BundledSQLiteDriver())
//        .build()
//    val habitDao: HabitDao = database.habitDao()
//    val habitHistoryEntryDao: HabitHistoryEntryDao = database.habitHistoryEntryDao()

    //todo: make a call to repository here
    val habitsList = mutableStateListOf<Habit>()


    val notificationsManager = NotificationsManager()


    // todo: the init block should be done with repository functions too
//    init {
//        notificationsManager.createAdviceChannel(applicationContext)
//        notificationsManager.createNotificationChannel(applicationContext)
//
//        //todo: test action, remove it
//        val builder = NotificationCompat.Builder(applicationContext, notificationsManager.ADVICE_CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_account_box)
//            .setContentTitle("My notification")
//            .setContentText("Much longer text that cannot fit one line...")
//            .setStyle(NotificationCompat.BigTextStyle()
//                .bigText("Much longer text that cannot fit one line..."))
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//
//        with(NotificationManagerCompat.from(applicationContext)) {
//            if (ActivityCompat.checkSelfPermission(
//                    applicationContext,
//                    android.Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                return@with
//            }
//            // notificationId is a unique int for each notification that you must define.
//            notify(notificationsManager.getNewAdviceId(), builder.build())
//        }
//    }

    init {
        repository.checkFailureStreaksAndNotify()
    }

    fun loadHabitList(): List<Habit> {
        viewModelScope.launch {
            val habitDataList = repository.loadHabitList()

            habitsList.clear()
            //translate all HabitData objects from the database
            //to format used by the application
            habitDataList.map {
                habitsList.add(Habit(it.key, it.value))
            }
        }
        return habitsList
    }

    fun addHabitAndReload(habit: Habit) {
        viewModelScope.launch {
            repository.addHabitAndReload(habit)
            loadHabitList()
        }
    }

    fun deleteHabitsAndReload(habits: List<Habit>) {
        viewModelScope.launch {
            repository.deleteHabitsAndReload(habits)
            loadHabitList()
        }
    }

    fun updateHabitAndReload(habit: Habit) {
        viewModelScope.launch {
            repository.updateHabitAndReload(habit)
            loadHabitList()
        }
    }

    fun addEntry(entry: HabitHistoryEntry) {
        viewModelScope.launch {
            repository.addEntry(entry)
            loadHabitList()
        }
    }

    fun deleteEntry(entry: HabitHistoryEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
            loadHabitList()
        }
    }
}