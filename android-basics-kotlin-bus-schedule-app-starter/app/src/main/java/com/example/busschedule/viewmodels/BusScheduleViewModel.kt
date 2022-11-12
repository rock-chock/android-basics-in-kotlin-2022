package com.example.busschedule.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.busschedule.database.Schedule
import com.example.busschedule.database.ScheduleDao
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalArgumentException

// A viewModel that is lifecycle aware and allows interacting with database
class BusScheduleViewModel(private val scheduleDao: ScheduleDao): ViewModel() {

    // Return asynchronous Flow to continuously monitor changes in the database
    fun fullSchedule(): Flow<List<Schedule>> = scheduleDao.getAll()

    fun scheduleForStopName(name: String): Flow<List<Schedule>> = scheduleDao.getByStopName(name)
}

// Use a factory class to instantiate a BusScheduleViewModel from an object that can respond to lifecycle events
// Instantiate it in this class (not in a fragment) to ease the process of memory management
class BusScheduleViewModelFactory(private val scheduleDao: ScheduleDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusScheduleViewModel(scheduleDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}