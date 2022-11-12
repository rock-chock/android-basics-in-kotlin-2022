package com.example.busschedule

import android.app.Application
import com.example.busschedule.database.AppDatabase

// Provide a custom subclass of the Application class, and create a lazy property that will hold the result of getDatabase()
class BusScheduleApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}